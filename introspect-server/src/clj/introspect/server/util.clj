(ns introspect.server.util)

(defn parse-db [url]
  (let [db-regex #"postgres://(\w+):(\w+)@([A-Za-z0-9\.\-]+):(\d+)/(\w+)"
        [_ user password host port db] (re-matches db-regex url)]
    {:user user
     :password password
     :host host
     :port port
     :db db}))

