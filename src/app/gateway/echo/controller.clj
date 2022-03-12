(ns app.gateway.echo.controller
  (:require [ring.util.http-response :as response]))

(defn routes [_]
  ["/echo/:id"
   {:get
    (fn [{{:keys [id]} :path-params}]
      (response/ok (str "<p>the value is: " id "</p>")))}])