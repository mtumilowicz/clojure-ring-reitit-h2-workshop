(ns app.infrastructure.person.module
  (:require
    [app.infrastructure.person.repository.inmemory :as PersonInMemoryRepository]
    [app.infrastructure.person.repository.db :as PersonDbRepository]))

(def inMemoryRepository (PersonInMemoryRepository/create))

(def dbRepository (PersonDbRepository/create))