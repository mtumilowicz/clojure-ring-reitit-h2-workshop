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

(defmulti from-domain-result (fn [[either _]] either))
(defmethod from-domain-result :left
  [[_ result]]
  (bad-request result))

(defmethod from-domain-result :right
  [[_ result]]
  (response-ok {:key "persons" :data result}))