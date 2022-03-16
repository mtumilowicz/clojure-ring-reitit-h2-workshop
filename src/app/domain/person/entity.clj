(ns app.domain.person.entity
  (:require
    [struct.core :as st]))

(defn Person [{:keys [id firstName lastName]}]
  {:id id :firstName firstName :lastName lastName }
  )

(def NewPersonCommandSchema
  {:firstName [st/required st/string]
   :lastName [st/required st/string]}
  )

(defn NewPersonCommand [{:keys [firstName lastName]}]
  {:firstName firstName :lastName lastName})