(ns app.gateway.middleware
  (:require [muuntaja.middleware :as muuntaja]))

(defn wrap-formats [handler]
  (muuntaja/wrap-format handler))