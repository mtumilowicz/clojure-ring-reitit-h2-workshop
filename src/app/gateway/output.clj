(ns app.gateway.output
  (:require [app.domain.either :as either]
            [ring.util.http-response :as response]))

(defn success [key data]
  {:status "success" :data {key data}})

(defn failure [failures]
  {:status "fail" :data {:errors failures}})

(defn response-ok [key data]
  (response/ok (success key data)))

(defn bad-request [errors]
  (response/bad-request (failure errors)))

(defn from-domain-result [key result]
  (either/fold bad-request
               (partial response-ok key)
               result))