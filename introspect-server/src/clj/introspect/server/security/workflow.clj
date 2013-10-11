(ns introspect.server.security.workflow
  (:require [cemerick.friend.workflows :as workflows]
            [cemerick.friend.util :refer (gets)]
            [ring.util.response :as response]
            [clojure.tools.logging :as log]
            [introspect.server.request :as r]))

(defn edn-response 
  [& {:keys [login-uri] :as edn-config}]
  (fn [request]
    (if (= (:uri request) login-uri)
      (if-let [{:keys [username password] :as creds} (-> request :params :data r/parse-body)]
        (if-let [user-record (and username password
                                  ((gets :credential-fn edn-config (:cemerick.friend/auth-config request))
                                   (with-meta creds {:cemerick.friend/workflow :edn-response})))]
          (workflows/make-auth user-record
                               {:cemerick.friend/workflow :edn-response
                                :cemerick.friend/redirect-on-auth? false})
          {:status 403
           :headers {"Content-Type" "application/edn"}
           :body (pr-str {:error "Incorrect login data"})})))))



