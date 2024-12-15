(ns app.infrastructure.person.module
  (:require
    [app.infrastructure.person.repository.db :as PersonDbRepository]
    [app.infrastructure.person.repository.inmemory :as PersonInMemoryRepository]))

(def in-memory-repository (PersonInMemoryRepository/create))

(def db-repository (PersonDbRepository/create))