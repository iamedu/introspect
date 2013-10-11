(ns introspect.server.request
  (:require [clojure.tools.logging :as log]
            [clojure.edn :as edn]))

(defprotocol EdnRead
  (-read-edn [this]))

(extend-type String
  EdnRead
  (-read-edn [s]
    (edn/read-string s)))

(extend-type java.io.InputStream
  EdnRead
  (-read-edn [is]
    (clojure.edn/read
      {:eof nil}
      (java.io.PushbackReader.
        (java.io.InputStreamReader.
          is "UTF-8")))))

(defn parse-body [body]
  (if-not (nil? body)
    (let [edn-params (binding [*read-eval* false]
                       (-read-edn body))]
      edn-params)))

(defn wrap-edn-params
  [handler]
  (fn [req]
    (if-let [body (-> req :params :data)]
      (let [edn-params (binding [*read-eval* false] (-read-edn body))
            req* (assoc req
                        :edn-params edn-params
                        :params (merge (:params req) edn-params))]
        (handler req*))
      (handler req))))


