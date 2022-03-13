(ns app.domain.either)

(defn success [result]
  [:right result])

(defn failure [errors]
  [:left errors])

(defn safe-try [{:keys [operation error-message]}]
  (try
    [:right (operation)]
    (catch Exception e [:left (str error-message (.getMessage e))])))