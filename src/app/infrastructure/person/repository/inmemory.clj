(ns app.infrastructure.person.repository.inmemory
  (:require
    [app.domain.person.repository :refer [PersonRepository]]
    [app.domain.either :as either]))

(def persons (atom {}))

(deftype PersonInMemoryRepository []
  PersonRepository
  (save! [_ {:keys [id] :as obj}]
    (->> (either/safe-execute {:operation     (swap! persons assoc-in [(keyword (str id))] obj)
                               :error-message "error while creating person: "})
         (either/map (constantly obj))))
  (getAll [_]
    (->> @persons
         (into (sorted-map))
         (vals)
         (into [])))
  (deleteById [_ id]
    (swap! persons dissoc (keyword (str id)))
    {:id id}))

(defn create [] (PersonInMemoryRepository.))