(ns app.domain.person.new-person-command
  (:require
    [struct.core :as st]))

(def Schema
  {:firstName [st/required st/string]
   :lastName  [st/required st/string]})

(defn create [{:keys [firstName lastName]}]
  {:firstName firstName :lastName lastName})

(defn assign-id [id newPersonCommand]
  (merge newPersonCommand {:id id}))