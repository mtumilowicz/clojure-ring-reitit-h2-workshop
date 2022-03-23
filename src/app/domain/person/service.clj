(ns app.domain.person.service
  (:require
    [app.domain.person.repository :as PersonRepository]
    [app.domain.id.service :as IdService]
    [app.domain.parser :as Parser]
    [app.domain.person.new-person-command :as NewPersonCommand]
    [app.domain.either :as Either]))

(defn assignId [dependencies newPersonCommand]
  (let [id (IdService/generate dependencies)]
    (NewPersonCommand/assign-id id newPersonCommand)))

(defn save [{:keys [personRepository] :as dependencies} newPersonCommand]
  (->> newPersonCommand
       (Parser/parse NewPersonCommand/Schema)
       (Either/map #(assignId dependencies %))
       (Either/flat-map #(PersonRepository/save! personRepository %))))

(defn getAll [{:keys [personRepository]}]
  (PersonRepository/getAll personRepository))

(defn deleteById [{:keys [personRepository]} id]
  (PersonRepository/deleteById personRepository id))