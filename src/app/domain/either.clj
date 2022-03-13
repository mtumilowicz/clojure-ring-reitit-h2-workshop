(ns app.domain.either)

(defn success [result]
  [:right result])

(defn failure [errors]
  [:left errors])

(defmacro to-either [{:keys [operation error-message]}]
  `(try
     (success ~operation)
    (catch Exception e# (failure (str ~error-message (.getMessage e#))))))