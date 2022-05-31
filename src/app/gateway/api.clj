(ns app.gateway.api
  (:require [reitit.ring :as reitit]
            [ring.util.http-response :as response]
            [app.gateway.person.controller :as PersonController]
            [app.gateway.echo.controller :as EchoController]
            [reitit.dev.pretty :as pretty]
            [reitit.ring.middleware.dev]
            [app.gateway.middleware :refer [wrap-formats]]))

(defn routes [dependencies]
  [(EchoController/routes dependencies)
   (PersonController/routes dependencies)])


(defn handler [dependencies]
  (reitit/ring-handler
    (reitit/router (routes dependencies)
                   {:exception pretty/exception
                    :data {:middleware [wrap-formats]}})
    (reitit/create-default-handler
      {:not-found
       (constantly (response/not-found "404 - Page not found"))
       :method-not-allowed
       (constantly (response/method-not-allowed "405 - Not allowed"))
       :not-acceptable
       (constantly (response/not-acceptable "406 - Not acceptable"))})))