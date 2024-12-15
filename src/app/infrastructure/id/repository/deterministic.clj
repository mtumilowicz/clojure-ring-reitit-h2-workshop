(ns app.infrastructure.id.repository.deterministic
  (:require
    [app.domain.id.repository :as id-repository]
    ))

(def counter (atom 0))

(defn generate []
  (swap! counter inc)
  (str @counter))

(defn create []
  (id-repository/create generate))