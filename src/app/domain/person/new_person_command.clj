(ns app.domain.person.new-person-command
  (:require
    [struct.core :as st]))

(def Schema
  {:firstName [st/required st/string]
   :lastName  [st/required st/string]})

(defrecord NewPersonCommand [firstName lastName])

(defn assign-id [id new-person-command]
  (merge new-person-command {:id id}))