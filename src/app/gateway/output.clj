(ns app.gateway.output
  (:require [ring.util.http-response :as response]))

(defn success [key data]
  {:status "success" :data {key data}})

(defn failure [failures]
  {:status "fail" :data {:errors failures}})

(defn response-ok [{:keys [key data]}]
  (response/ok (success key data)))

(defn bad-request [errors]
  (response/bad-request (failure errors)))

(defmulti from-domain (fn [[either _]] either))
(defmethod from-domain :left
  [[_ result]]
  (bad-request result))

(defmethod from-domain :right
  [[_ result]]
  (response-ok {:key "persons" :data result}))