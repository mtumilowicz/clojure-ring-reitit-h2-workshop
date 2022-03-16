(ns app.domain.person.service
  (:require
    [app.domain.person.repository :as PersonRepository]
    [app.domain.id.service :as IdService]
    [app.domain.parser :as Parser]
    [app.domain.person.entity :refer [NewPersonCommandSchema]]
    [app.domain.either :as Either]))

(defn assignId [dependencies newPersonCommand]
  (let [id (IdService/generate dependencies)]
    (merge newPersonCommand {:id id})))

(defn save [{:keys [personRepository] :as dependencies} newPersonCommand]
  (->> newPersonCommand
       (Parser/parse NewPersonCommandSchema)
       (Either/map #(assignId dependencies %))
       (Either/flat-map #(PersonRepository/save! personRepository %))))

(defn getAll [{:keys [personRepository]}]
  (PersonRepository/getAll personRepository))

(defn deleteById [{:keys [personRepository]} id]
  (PersonRepository/deleteById personRepository id))