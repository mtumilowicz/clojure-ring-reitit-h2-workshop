(ns app.infrastructure.person.module
  (:require
    [app.infrastructure.person.repository.db :as PersonDbRepository]
    [app.infrastructure.person.repository.inmemory :as PersonInMemoryRepository]))

(def inMemoryRepository (PersonInMemoryRepository/create))

(def dbRepository (PersonDbRepository/create))