(ns app.domain.person.entity
  (:require
    [struct.core :as st]))

(defn Person [{:keys [id firstName lastName]}]
  {:id id :firstName firstName :lastName lastName }
  )

(def NewPersonCommandSchema
  {:id      [st/required st/integer]
   :firstName [st/required st/string]
   :lastName [st/required st/string]}
  )

(defn NewPersonCommand [{:keys [id firstName lastName]}]
  {:id id :firstName firstName :lastName lastName})