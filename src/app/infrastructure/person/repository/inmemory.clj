(ns app.infrastructure.person.repository.inmemory
  (:require
    [app.domain.either :as either]
    [app.domain.person.repository :as person-repository]
    ))

(defn save-person! [map obj]
  (->> (either/safe-execute {:operation     (swap! map assoc-in [(keyword (str (:id obj)))] obj)
                             :error-message "error while creating person: "})
       (either/map (constantly obj))))

(defn get-all-persons [map]
  (->> @map
       (into (sorted-map))
       vals
       (into [])))

(defn delete-person-by-id! [map id]
  (swap! map dissoc (keyword (str id)))
  {:id id})

(defn create-inmemory-repository []
  (let [map (atom {})]
    (person-repository/create
      (partial save-person! map)
      (partial get-all-persons map)
      (partial delete-person-by-id! map))))
