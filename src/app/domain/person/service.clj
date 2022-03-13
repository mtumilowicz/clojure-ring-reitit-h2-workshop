(ns app.domain.person.service
  (:require [app.domain.person.repository :as PersonRepository]))

(defn save [personRepository person]
  (PersonRepository/save! personRepository person))

(defn getAll [personRepository]
  (PersonRepository/getAll personRepository))

(defn deleteById [personRepository id]
  (PersonRepository/deleteById personRepository id))