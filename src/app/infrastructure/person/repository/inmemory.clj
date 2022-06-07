(ns app.infrastructure.person.repository.inmemory
  (:require
    [app.domain.person.repository :refer [PersonRepository]]
    [app.domain.either :as either]))

(defrecord PersonInMemoryRepository [map]
  PersonRepository
  (save! [_ {:keys [id] :as obj}]
    (->> (either/safe-execute {:operation     (swap! map assoc-in [(keyword (str id))] obj)
                               :error-message "error while creating person: "})
         (either/map (constantly obj))))
  (getAll [_]
    (->> @map
         (into (sorted-map))
         (vals)
         (into [])))
  (deleteById [_ id]
    (swap! map dissoc (keyword (str id)))
    {:id id}))

(defn create [map] (PersonInMemoryRepository. map))