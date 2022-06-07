(ns app.infrastructure.id.repository.deterministic
  (:require
    [app.domain.id.repository :refer [IdRepository]]))

(def counter (atom 0))

(defrecord DeterministicIdRepository []
  IdRepository
  (generate [_]
    (swap! counter inc)
    (str @counter)))

(defn create [] (DeterministicIdRepository.))

