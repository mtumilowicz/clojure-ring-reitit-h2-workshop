(ns app.domain.person.service
  (:require
    [app.domain.person.repository :as PersonRepository]
    [app.domain.id.service :as IdService]
    [app.domain.parser :as Parser]
    [app.domain.person.entity :refer [NewPersonCommandSchema]]
    [app.domain.either :as Either]))

(defn assignId [idRepository newPersonCommand]
  (let [id (IdService/generate idRepository)]
    (merge newPersonCommand {:id id})))

(defn save [personRepository idRepository newPersonCommand]
  (->> newPersonCommand
       (Parser/parse NewPersonCommandSchema)
       (Either/map #(assignId idRepository %))
       (Either/flat-map #(PersonRepository/save! personRepository %))))

(defn getAll [personRepository]
  (PersonRepository/getAll personRepository))

(defn deleteById [personRepository id]
  (PersonRepository/deleteById personRepository id))