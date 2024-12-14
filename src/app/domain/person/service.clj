(ns app.domain.person.service
  (:require
    [app.domain.person.repository :as PersonRepository]
    [app.domain.id.service :as IdService]
    [app.domain.parser :as Parser]
    [app.domain.person.new-person-command :as NewPersonCommand]
    [app.domain.either :as Either]))

(defn assignId [idRepository newPersonCommand]
  (let [id (:generate (IdService/create-id-service idRepository))]
    (NewPersonCommand/assign-id id newPersonCommand)))

(defn save [personRepository idRepository newPersonCommand]
  (->> newPersonCommand
       (Parser/parse NewPersonCommand/Schema)
       (Either/map #(assignId idRepository %))
       (Either/flat-map #(PersonRepository/save! personRepository %))))

(defn getAll [personRepository]
  (PersonRepository/getAll personRepository))

(defn deleteById [personRepository id]
  (PersonRepository/deleteById personRepository id))