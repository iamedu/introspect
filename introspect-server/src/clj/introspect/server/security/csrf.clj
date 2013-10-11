(ns introspect.server.security.csrf)
  
;; src/barebones_shoreleave/middleware.clj 
(defn wrap-add-anti-forgery-cookie 
  "Mimics code in Shoreleave-baseline's
customized ring-anti-forgery middleware."
  [handler & [opts]]
  (fn [request]
    (let [response (handler request)]
      (if-let [token (-> request :session (get "__anti-forgery-token"))]
        (assoc-in response [:cookies "__anti-forgery-token"] {:value token
                                                              :path "/"})
        response))))

