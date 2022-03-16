(ns app.gateway.person.input
  (:require
    [struct.core :as st]
    [app.domain.person.entity :as Entity]))

(def NewPersonApiInput
  {:first_name [st/required st/string]
   :last_name  [st/required st/string]})

(defn to-NewPersonCommand [newPersonApiInput]
  (Entity/NewPersonCommand (clojure.set/rename-keys newPersonApiInput {:first_name :firstName
                                                                       :last_name  :lastName})))