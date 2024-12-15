(ns app.infrastructure.person.repository.db
  (:require
    [app.domain.either :as either]
    [app.domain.person.repository :as person-repository]
    [app.domain.person.person :as Person]
    [app.infrastructure.db.config :as db]
    [clojure.set :as set]))

(defn dbPerson-toDomain [dbPerson]
  (Person/map->Person (set/rename-keys dbPerson {:first_name :firstName
                                                 :last_name  :lastName})))

(defn save-person! [obj]
  (->> (either/safe-execute {:operation     (db/create-person! obj)
                             :error-message "error while creating person: "})
       (either/map (constantly obj))))

(defn get-all-persons []
  (->> (db/get-persons)
       (map dbPerson-toDomain)
       (into [])))

(defn delete-person-by-id! [id]
  (db/delete-by-id! {:id id})
  {:id id})

(defn create []
  (person-repository/create
    save-person!
    get-all-persons
    delete-person-by-id!))
