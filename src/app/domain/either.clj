(ns app.domain.either)

(defn success [result]
  [:right result])

(defn failure [errors]
  [:left errors])

(defmulti fold-either (fn [[either _] _ _] either))
(defmethod fold-either :left
  [[_ result] handle-left _]
  (handle-left result))
(defmethod fold-either :right
  [[_ result] _ handle-right]
  (handle-right result))

(defmacro to-either [{:keys [operation error-message]}]
  `(try
     (success ~operation)
    (catch Exception e# (failure (str ~error-message (.getMessage e#))))))