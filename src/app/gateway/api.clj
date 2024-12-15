(ns app.gateway.api
  (:require [app.gateway.echo.controller :as EchoController]
            [app.gateway.middleware :refer [wrap-formats]]
            [app.gateway.person.controller :as PersonController]
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as reitit]
            [reitit.ring.middleware.dev]
            [ring.util.http-response :as response]))

(defn routes [person-service]
  [(EchoController/routes [])
   (PersonController/routes person-service)])


(defn handler [person-service]
  (reitit/ring-handler
    (reitit/router (routes person-service)
                   {:exception pretty/exception
                    :data      {:middleware [wrap-formats]}})
    (reitit/create-default-handler
      {:not-found
       (constantly (response/not-found "404 - Page not found"))
       :method-not-allowed
       (constantly (response/method-not-allowed "405 - Not allowed"))
       :not-acceptable
       (constantly (response/not-acceptable "406 - Not acceptable"))})))