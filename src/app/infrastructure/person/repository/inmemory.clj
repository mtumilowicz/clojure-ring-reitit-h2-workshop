(ns app.infrastructure.person.repository.inmemory
  (:require
    [app.domain.either :as either]))

(defn create-inmemory-repository []
  (let [map (atom {})] ; In-memory storage
    {:save! (fn [{:keys [id] :as obj}]
              (->> (either/safe-execute {:operation     (swap! map assoc-in [(keyword (str id))] obj)
                                         :error-message "error while creating person: "})
                   (either/map (constantly obj))))
     :getAll (fn []
               (->> @map
                    (into (sorted-map))
                    vals
                    (into [])))
     :deleteById (fn [id]
                   (swap! map dissoc (keyword (str id)))
                   {:id id})}))