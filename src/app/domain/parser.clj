(ns app.domain.parser
  (:require
    [struct.core :as st]
    [clojure.core.match :refer [match]]
    [app.domain.either :as either]))

(defn parse [map schema]
  (match (st/validate map schema)
         [nil valid] (either/right map)
         [errors _] (either/left errors)))
