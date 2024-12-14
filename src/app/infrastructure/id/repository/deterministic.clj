(ns app.infrastructure.id.repository.deterministic)

(def counter (atom 0))

(defn generate []
  (swap! counter inc)
  (str @counter))

(defn create-deterministic-id-repository []
  {:generate generate})