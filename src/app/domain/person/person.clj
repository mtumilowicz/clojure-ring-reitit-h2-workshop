(ns app.domain.person.person)

(defn create [{:keys [id firstName lastName]}]
  {:id id :firstName firstName :lastName lastName})