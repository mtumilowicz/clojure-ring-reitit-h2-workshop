(ns app.infrastructure.id.module
  (:require
    [app.infrastructure.id.repository.deterministic :as DeterministicIdRepository]
    [app.infrastructure.id.repository.uuid :as UuidIdRepository]))

(def deterministicRepository (DeterministicIdRepository/create))

(def uuidRepository (UuidIdRepository/create))