(ns app.domain.either)

(defn right [result]
  [:right result])

(defn left [errors]
  [:left errors])

(defmulti fold (fn [[either _] _ _] either))
(defmethod fold :left
  [[_ result] handle-left _]
  (handle-left result))
(defmethod fold :right
  [[_ result] _ handle-right]
  (handle-right result))

(defmacro safe-execute [{:keys [operation error-message]}]
  `(try
     (right ~operation)
     (catch Exception e# (left (str ~error-message (.getMessage e#))))))