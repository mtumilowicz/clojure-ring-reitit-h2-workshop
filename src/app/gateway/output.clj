(ns app.gateway.output
  (:require [ring.util.http-response :as response]
            [app.domain.either :refer [fold-either]]))

(defn success [key data]
  {:status "success" :data {key data}})

(defn failure [failures]
  {:status "fail" :data {:errors failures}})

(defn response-ok [{:keys [key data]}]
  (response/ok (success key data)))

(defn bad-request [errors]
  (response/bad-request (failure errors)))

(defn from-domain-result [result]
  (fold-either result
               bad-request
               #(response-ok {:key "persons" :data %}))
  )