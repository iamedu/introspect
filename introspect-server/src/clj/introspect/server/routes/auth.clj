(ns introspect.server.routes.auth
  (:use compojure.core)
  (:require  [cemerick.friend :as friend]))

(defroutes auth-routes
  (POST "/login" request
        {:body  (friend/identity request) 
         :session  (:session request)})
  (POST "/identity" request
        {:body  (friend/identity request)})
  (friend/logout  (ANY "/logout" request "Logged out")))

