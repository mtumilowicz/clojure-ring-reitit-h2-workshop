(ns app.infrastructure.person.module
  (:require
    [app.domain.person.repository :refer [PersonRepository]]
    [app.infrastructure.db.config :refer [*db*] :as db]))

(def app-state (atom {}))

(deftype PersonInMemoryRepository []
  PersonRepository
  (save! [_ {:keys [id] :as obj}]
    (swap! app-state assoc-in [(keyword (str id))] obj)
    obj
    )
  (getAll [_] @app-state)
  (deleteById [_ id] (swap! app-state dissoc (keyword (str id)))))

(deftype PersonDbRepository []
  PersonRepository
  (save! [_ obj] (db/create-person! obj))
  (getAll [_] (db/get-persons))
  (deleteById [_ id] (db/delete-by-id! {:id id})))

(def inMemoryRepository (PersonInMemoryRepository.))

(def dbRepository (PersonDbRepository.))