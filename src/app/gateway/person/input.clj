(ns app.gateway.person.input
  (:require
    [app.domain.person.new-person-command :as NewPersonCommand]
    [struct.core :as st]))

(def NewPersonApiInputSchema
  {:first_name [st/required st/string]
   :last_name  [st/required st/string]})

(defn to-NewPersonCommand [new-person-api-input]
  (NewPersonCommand/map->NewPersonCommand (clojure.set/rename-keys new-person-api-input {:first_name :firstName
                                                                                         :last_name  :lastName})))