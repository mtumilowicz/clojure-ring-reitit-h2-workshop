(ns app.infrastructure.id.module
  (:require
    [app.infrastructure.id.repository.deterministic :as DeterministicIdRepository]
    [app.infrastructure.id.repository.uuid :as UuidIdRepository]))

(def deterministic-repository (DeterministicIdRepository/create))

(def uuid-repository (UuidIdRepository/create))