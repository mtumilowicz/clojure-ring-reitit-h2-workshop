(ns app.gateway.api
  (:require [reitit.ring :as reitit]
            [ring.util.http-response :as response]
            [app.gateway.person.controller :as PersonController]
            [app.gateway.echo.controller :as EchoController]))

(defn routes [dependencies]
  [(EchoController/routes dependencies)
   (PersonController/routes dependencies)])


(defn handler [dependencies]
  (reitit/ring-handler
    (reitit/router (routes dependencies))
    (reitit/create-default-handler
      {:not-found
       (constantly (response/not-found "404 - Page not found"))
       :method-not-allowed
       (constantly (response/method-not-allowed "405 - Not allowed"))
       :not-acceptable
       (constantly (response/not-acceptable "406 - Not acceptable"))})))