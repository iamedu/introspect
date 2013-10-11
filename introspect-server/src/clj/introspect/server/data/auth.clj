(ns introspect.server.data.auth
  (:use korma.core
        introspect.server.data.schema)
  (:require [clojure.tools.logging :as log]))

(defn find-user [username]     
  (first (select users         
                 (with roles)  
                 (where {:username username
                         :active true})))) 
