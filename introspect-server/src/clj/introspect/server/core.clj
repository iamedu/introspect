(ns introspect.server.core
  (:use org.httpkit.server
        compojure.core)
  (:require [clojure.tools.logging :as log]
            [introspect.server.util :as util]
            [clojure.tools.cli :refer (cli)]))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defn -main [& args]
  (let [[options args banner] (cli args
                                   ["-p" "--port" "Port to listen on" :default 3000 :parse-fn #(Integer/parseInt %)]
                                   ["-d" "--database" "Database url" :parse-fn util/parse-db])
        {:keys [port database]} options]
    (log/info "Starting server on port and database" port (:db database))))
