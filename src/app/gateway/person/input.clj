(ns app.gateway.person.input
  (:require
    [struct.core :as st]
    [app.domain.person.entity :as Entity]))

(def CreatePersonApiInput
  {:id      [st/required st/integer]
   :first_name [st/required st/string]
   :last_name [st/required st/string]})

(defn to-CreatePersonCommand [createPersonApiInput]
  (Entity/make-CreatePersonCommand (clojure.set/rename-keys createPersonApiInput {:first_name :firstName
                                                                                  :last_name :lastName})))