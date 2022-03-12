(ns app.infrastructure.message.module
  (:require
    [app.domain.message.repository :refer [MessageRepository]]
    [app.infrastructure.db.config :refer [*db*] :as db]))

(def app-state (atom {}))

(deftype MessageInMemoryRepository []
  MessageRepository
  (save! [_ {:keys [id] :as obj}]
    (swap! app-state assoc-in [(keyword (str id))] obj)
    obj
    )
  (getAll [_] @app-state))

(def inMemoryRepository (MessageInMemoryRepository.))

(deftype MessageDbRepository []
  MessageRepository
  (save! [_ obj] (db/save-message! obj))
  (getAll [_] (db/get-messages)))

(def dbRepository (MessageDbRepository.))