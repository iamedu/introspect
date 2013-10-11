(ns introspect.server.core
  (:use org.httpkit.server
        compojure.core)
  (:require [clojure.tools.logging :as log]
            [clojure.tools.cli :refer (cli)]
            [compojure.route :as route]
            [introspect.server.util :as util]))

(defroutes app
  (GET "/"  [] "<h1>Hello World</h1>")
  (route/not-found "<h1>Page not found</h1>"))

(defn -main [& args]
  (let [[options args banner] (cli args
                                   ["-p" "--port" "Port to listen on" :default 3000 :parse-fn #(Integer/parseInt %)]
                                   ["-d" "--database" "Database url" :parse-fn util/parse-db])
        {:keys [port database]} options]
    (log/info "Starting server on port" port "database" (:db database))
    (run-server app {:port port})))
