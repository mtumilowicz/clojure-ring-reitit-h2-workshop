(ns app.gateway.output
  (:require [ring.util.http-response :as response]))

(defn ApiOutput [{:keys [status data]}]
  {:status status :data data})

(defn success [key data]
  (ApiOutput {:status "success" :data {key data}}))

(defn failure [failures]
  (ApiOutput {:status "fail" :data {:errors failures}}))

(defn response-ok [{:keys [key data]}]
  (response/ok (success key data)))

(defn bad-request [errors]
  (response/bad-request (failure errors)))