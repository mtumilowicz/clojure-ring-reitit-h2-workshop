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
  (getAll [_] @app-state)
  (deleteById [_ id] (swap! app-state dissoc (keyword (str id)))))

(deftype MessageDbRepository []
  MessageRepository
  (save! [_ obj] (db/save-message! obj))
  (getAll [_] (db/get-messages))
  (deleteById [_ id] (db/delete-by-id! {:id id})))

(def inMemoryRepository (MessageInMemoryRepository.))

(def dbRepository (MessageDbRepository.))