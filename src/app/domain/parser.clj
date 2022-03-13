(ns app.domain.parser
  (:require
    [struct.core :as st]
    [clojure.core.match :refer [match]]))

(defn parse [map schema]
  (match (st/validate map schema)
         [nil valid] [:right map]
         [errors _] [:left errors]))
