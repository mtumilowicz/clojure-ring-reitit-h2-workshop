(ns app.gateway.person.input
  (:require
    [struct.core :as st]
    [clojure.core.match :refer [match]]))

(def CreatePersonApiInput
  {:id      [st/required st/integer]
   :firstName [st/required st/string]
   :lastName [st/required st/string]})

(defn parse-CreatePersonApiInput [map]
  (match (st/validate map CreatePersonApiInput)
         [nil valid] [:right map]
         [errors _] [:left errors]))