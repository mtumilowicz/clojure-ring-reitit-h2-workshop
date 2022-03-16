(ns app.infrastructure.person.repository.db
  (:require
    [app.domain.person.repository :refer [PersonRepository]]
    [app.infrastructure.db.config :refer [*db*] :as db]
    [app.domain.either :as either]
    [app.domain.person.entity :as entity]))

(defn dbPerson-toDomain [dbPerson]
  (entity/Person (clojure.set/rename-keys dbPerson {:first_name :firstName
                                                    :last_name  :lastName})))

(deftype PersonDbRepository []
  PersonRepository
  (save! [_ obj]
    (->> (either/safe-execute {:operation     (db/create-person! obj)
                               :error-message "error while creating person: "})
         (either/map (constantly obj))))
  (getAll [_]
    (map dbPerson-toDomain (db/get-persons)))
  (deleteById [_ id]
    (db/delete-by-id! {:id id})
    {:id id}))

(defn create [] (PersonDbRepository.))
