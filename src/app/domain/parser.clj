(ns app.domain.parser
  (:require
    [struct.core :as st]
    [clojure.core.match :refer [match]]
    [app.domain.either :refer :all]))

(defn parse [map schema]
  (match (st/validate map schema)
         [nil valid] (success map)
         [errors _] (failure errors)))
