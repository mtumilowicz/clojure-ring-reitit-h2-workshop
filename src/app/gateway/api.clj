(ns app.gateway.api
  (:require [reitit.ring :as reitit]
            [ring.util.http-response :as response]
            [app.gateway.person.controller :as PersonController]
            [app.gateway.echo.controller :as EchoController]
            [reitit.dev.pretty :as pretty]
            [reitit.ring.middleware.dev]
            [app.gateway.middleware :refer [wrap-formats]]))

(defn routes [person-service id-service]
  [(EchoController/routes [])
   (PersonController/routes person-service)])


(defn handler [person-service id-service]
  (reitit/ring-handler
    (reitit/router (routes person-service id-service)
                   {:exception pretty/exception
                    :data {:middleware [wrap-formats]}})
    (reitit/create-default-handler
      {:not-found
       (constantly (response/not-found "404 - Page not found"))
       :method-not-allowed
       (constantly (response/method-not-allowed "405 - Not allowed"))
       :not-acceptable
       (constantly (response/not-acceptable "406 - Not acceptable"))})))