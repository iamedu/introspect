(ns introspect.server.core
  (:use org.httpkit.server
        compojure.core)
  (:require [clojure.tools.logging :as log]
            [clojure.tools.cli :refer (cli)]
            [clojure.java.io :as io]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            (cemerick.friend  [credentials :as creds])
            [introspect.server.schema :as schema]
            [introspect.server.util :as util]))

(defonce sessions (atom {}))

(defroutes app-routes
  (GET "/" []
       (io/resource "public/index.html"))
  (route/resources "/")
  (route/not-found 
    (io/resource "public/404.html")))

(defn -main [& args]
  (let [[options args banner] (cli args
                                   ["-p" "--port" "Port to listen on" :default 3000 :parse-fn #(Integer/parseInt %)]
                                   ["-d" "--database" "Database url" :parse-fn util/parse-db])
        {:keys [port database]} options]
    (log/info "Starting server on port" port "database" (:db database))
    (schema/setup-database database)
    (run-server app-routes {:port port})))
