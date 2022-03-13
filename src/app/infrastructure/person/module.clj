(ns app.infrastructure.person.module
  (:require
    [app.domain.person.repository :refer [PersonRepository]]
    [app.infrastructure.db.config :refer [*db*] :as db]
    [app.domain.either :refer :all]))

(def app-state (atom {}))

(deftype PersonInMemoryRepository []
  PersonRepository
  (save! [_ {:keys [id] :as obj}]
    (to-either {:operation (swap! app-state assoc-in [(keyword (str id))] obj) :error-message "error while creating person: "}))
  (getAll [_] @app-state)
  (deleteById [_ id] (swap! app-state dissoc (keyword (str id)))))

(deftype PersonDbRepository []
  PersonRepository
  (save! [_ obj]
    (to-either {:operation (db/create-person! obj) :error-message "error while creating person: "}))
  (getAll [_] (db/get-persons))
  (deleteById [_ id] (db/delete-by-id! {:id id})))

(def inMemoryRepository (PersonInMemoryRepository.))

(def dbRepository (PersonDbRepository.))