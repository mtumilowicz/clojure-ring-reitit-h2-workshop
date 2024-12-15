(ns app.domain.person.service
  (:require
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

(defn mkService [person-repository id-service]
  {:assign-id (partial assignId id-service)
   :save (partial save person-repository id-service)
   :get-all (partial getAll person-repository)
   :delete-by-id (partial deleteById person-repository)})