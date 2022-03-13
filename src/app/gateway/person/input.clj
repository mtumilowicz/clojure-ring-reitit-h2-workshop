(ns app.gateway.person.input
  (:require
    [struct.core :as st]
    [app.domain.parser :refer :all]
    [app.domain.person.entity :refer :all]))

(def CreatePersonApiInput
  {:id      [st/required st/integer]
   :first_name [st/required st/string]
   :last_name [st/required st/string]})

(defn to-CreatePersonCommand [{:keys [id first_name last_name]}]
  (make-CreatePersonCommand {:id id :firstName first_name :lastName last_name}))