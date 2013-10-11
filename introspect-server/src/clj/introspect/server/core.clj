(ns introspect.server.core
  (:use org.httpkit.server
        compojure.core)
  (:require [clojure.tools.logging :as log]
            [clojure.tools.cli :refer (cli)]
            [clojure.tools.nrepl.server :as repl]
            [clojure.java.io :as io]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.reload :as reload]
            [ring.middleware.session.memory :refer  (memory-store)]
            [noir.util.middleware :as middleware]
            [ring.middleware.gzip :as gzip]
            [ring.middleware.session :as session]
            [ring.middleware.head :as head]
            [ring.middleware.anti-forgery :as forgery]
            [cemerick.friend :as friend]
            (cemerick.friend  [credentials :as creds])
            [introspect.server.routes.auth :refer (auth-routes)]
            [introspect.server.request :as rq]
            [introspect.server.security.workflow :as wf]
            [introspect.server.security.csrf :as csrf]
            [introspect.server.data.schema :as schema]
            [introspect.server.data.auth :as auth]
            [introspect.server.util :as util]))

(defonce sessions (atom {}))

(defroutes app-routes
  (GET "/" []
       (io/resource "public/index.html"))
  (context "/api/v1/auth" [] auth-routes)
  (route/resources "/")
  (route/not-found 
    (io/resource "public/404.html")))

(def secured-routes
  (-> app-routes
      (friend/authenticate {:credential-fn (partial creds/bcrypt-credential-fn auth/find-user)
                            :workflows [(wf/edn-response
                                          :login-uri "/api/v1/auth/login")]})))

(def app-handler (middleware/app-handler
                   [secured-routes]
                   :formats [:edn]
                   :store (memory-store sessions)
                   :middleware [rq/wrap-edn-params
                                head/wrap-head
                                csrf/wrap-add-anti-forgery-cookie
                                forgery/wrap-anti-forgery
                                gzip/wrap-gzip]))

(defn -main [& args]
  (let [[options args banner] (cli args
                                   ["-p" "--port" "Port to listen on" :default 3000 :parse-fn #(Integer/parseInt %)]
                                   ["-n" "--nrepl-port" "Port for nrepl listen on" :parse-fn #(Integer/parseInt %)]
                                   ["-d" "--database" "Database url" :parse-fn util/parse-db]
                                   ["-r" "--profile" "Profile to use" :default :devel :parse-fn keyword])
        {:keys [port database profile nrepl-port]} options
        app (if (= profile :prod)
              (handler/site app-routes)
              (reload/wrap-reload
                (handler/site #'app-routes)))]
    (if nrepl-port
      (repl/start-server :port nrepl-port))
    (log/info "Starting server on port" port "database" (:db database) "profile" (name profile))
    (schema/setup-database database)
    (run-server app {:port port})))
