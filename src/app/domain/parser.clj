(ns app.domain.parser
  (:require
    [app.domain.either :as either]
    [clojure.core.match :refer [match]]
    [struct.core :as st]))

(defn parse [schema map]
  (match (st/validate map schema)
         [nil valid] (either/right map)
         [errors _] (either/left errors)))
