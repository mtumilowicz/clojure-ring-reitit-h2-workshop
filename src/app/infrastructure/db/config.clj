(ns app.infrastructure.db.config
  (:require
    [mount.core :refer [defstate]]
    [conman.core :as conman]
    [app.infrastructure.app.config :refer [env]]))

(defstate ^:dynamic *db*
          :start (conman/connect! {:jdbc-url (env :database-url)})
          :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")