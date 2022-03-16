(ns app.domain.person.service
  (:require
    [app.domain.person.repository :as PersonRepository]
    [app.domain.id.service :as IdService]))

(defn save [personRepository idRepository  newPersonCommand]
  (let [id (IdService/generate idRepository)
        person (merge newPersonCommand {:id id})]
  (PersonRepository/save! personRepository person)))

(defn getAll [personRepository]
  (PersonRepository/getAll personRepository))

(defn deleteById [personRepository id]
  (PersonRepository/deleteById personRepository id))