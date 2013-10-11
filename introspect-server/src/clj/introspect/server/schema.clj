(ns introspect.server.schema
  (:use korma.db
        korma.core)
  (:require [clojure.tools.logging :as log]))

(declare users roles tunnels)

(defentity users
  (pk :username)
  (many-to-many roles :role_assignments
                {:lfk "role_assignments.username"
                 :rfk "role_assignments.role_code"}))

(defentity roles
  (pk :role_code))

(defentity tunnels)

(defn setup-database [spec]
  (default-connection (create-db (assoc spec :make-pool? true))))
