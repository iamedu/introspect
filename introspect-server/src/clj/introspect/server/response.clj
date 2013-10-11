(ns introspect.server.response
  (:require [clojure.tools.logging :as log]
            [ring.util.response :refer (content-type)]))

(defn edn-response [data & [status]]
  {:status (or status 200) 
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})    
