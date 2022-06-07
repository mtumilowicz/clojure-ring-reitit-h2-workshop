(ns app.infrastructure.person.module
  (:require
    [app.infrastructure.person.repository.inmemory :as PersonInMemoryRepository]
    [app.infrastructure.person.repository.db :as PersonDbRepository]))

(defn inMemoryRepository [map] (PersonInMemoryRepository/create map))

(def dbRepository (PersonDbRepository/create))