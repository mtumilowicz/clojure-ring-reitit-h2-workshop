(ns app.domain.either
  (:require [clojure.core.match :refer [match]]))

(defn right [value]
  [:right value])

(defn left [errors]
  [:left errors])

(defn get-tag [this]
  (first this))

(defn get-result [this]
  (second this))

(defn transform [mapping this]
  (mapping (get-result this)))

(defmulti fold (fn [_ _ this] (get-tag this)))
(defmethod fold :left
  [handle-left _ this]
  (transform handle-left this))
(defmethod fold :right
  [_ handle-right this]
  (transform handle-right this))

(defn flat-map [mapping this]
  (match this
         [:left _] this
         [:right value] (mapping value)))

(defn map [mapping this]
  (flat-map (comp right mapping) this))

(defmacro safe-execute [{:keys [operation error-message]}]
  `(try
     (right ~operation)
     (catch Exception e# (left (str ~error-message (.getMessage e#))))))