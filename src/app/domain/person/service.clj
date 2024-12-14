(ns app.domain.person.service
  (:require
    [app.domain.person.repository :as PersonRepository]
    [app.domain.id.service :as IdService]
    [app.domain.parser :as Parser]
    [app.domain.person.new-person-command :as NewPersonCommand]
    [app.domain.either :as Either]))

(defn assignId [id-service newPersonCommand]
  (let [id ((:generate id-service))]
    (NewPersonCommand/assign-id id newPersonCommand)))

(defn save [personRepository id-service newPersonCommand]
  (->> newPersonCommand
       (Parser/parse NewPersonCommand/Schema)
       (Either/map #(assignId id-service %))
       (Either/flat-map #((:save! personRepository) %))))

(defn getAll [personRepository]
  ((:get-all personRepository)))

(defn deleteById [personRepository id]
  ((:delete-by-id personRepository) id))