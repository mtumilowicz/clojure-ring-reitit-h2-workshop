(ns app.infrastructure.db.config
  (:require
    [app.infrastructure.app.config :refer [env]]
    [conman.core :as conman]
    [mount.core :refer [defstate]]))

(defstate ^:dynamic *db*
          :start (conman/connect! {:jdbc-url (env :database-url)})
          :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")

(defn in-transaction [operations]
  (conman/with-transaction [*db*] (operations)))