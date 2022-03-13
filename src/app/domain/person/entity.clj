(ns app.domain.person.entity
  (:require
    [struct.core :as st]))

(def CreatePersonCommand
  {:id      [st/required st/integer]
   :firstName [st/required st/string]
   :lastName [st/required st/string]}
  )

(defn make-CreatePersonCommand [{:keys [id firstName lastName]}]
  {:id id :firstName firstName :lastName lastName})