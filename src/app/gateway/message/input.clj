(ns app.gateway.message.input
  (:require
    [struct.core :as st]))

(defrecord MessageApiInput [id message])

(def schema-messageApiInput
  {:id      [st/required st/integer]
   :message [st/required st/string]}
  )

(defn validate-message [params]
  (first (st/validate params schema-messageApiInput)))

(defn parse-messageApiInput [map]
  (if-let [errors (validate-message map)]
    [:left errors]
    [:right (map->MessageApiInput map)]))