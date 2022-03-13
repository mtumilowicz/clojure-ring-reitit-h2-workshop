(ns app.gateway.message.input
  (:require
    [struct.core :as st]
    [clojure.core.match :refer [match]]))

(def CreateMessageApiInput
  {:id      [st/required st/integer]
   :message [st/required st/string]})

(defn parse-CreateMessageApiInput [map]
  (match (st/validate map CreateMessageApiInput)
         [nil valid] [:right map]
         [errors _] [:left errors]))