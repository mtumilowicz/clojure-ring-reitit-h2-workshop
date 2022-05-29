(ns app.gateway.echo.controller
  (:require
    [ring.util.http-response :as response]
    [app.gateway.middleware :refer [wrap-params]]))

(defn routes [_]
  ["/echo/:id"
   {:middleware [wrap-params]
    :get
    (fn [request-map]
      (response/ok (str "path-params: " (:path-params request-map) ", query params: " (:query-params request-map))))}])