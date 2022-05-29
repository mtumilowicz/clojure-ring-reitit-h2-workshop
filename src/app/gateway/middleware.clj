(ns app.gateway.middleware
  (:require
    [muuntaja.middleware :as muuntaja]
    [ring.middleware.params :as ring]))

(defn wrap-formats [handler]
  (muuntaja/wrap-format handler))

(defn wrap-params [handler]
  (ring/wrap-params handler))