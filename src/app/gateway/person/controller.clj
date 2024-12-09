(ns app.gateway.person.controller
  (:require
    [app.domain.parser :as Parser]
    [app.gateway.middleware :refer [wrap-formats]]
    [app.domain.person.service :as PersonService]
    [app.gateway.person.input :as ApiInput]
    [app.gateway.output :refer :all]
    [clojure.core.match :refer [match]]
    [app.domain.either :as either]))

(defn create-person [personRepository idRepository request-map]
  (->> request-map
       (:body-params)
       (Parser/parse ApiInput/NewPersonApiInputSchema)
       (either/map ApiInput/to-NewPersonCommand)
       (either/flat-map #(PersonService/save personRepository idRepository %))
       (from-domain-result "person")))

(defn routes [personRepository idRepository]
  ["/api"
   ["/persons"
    {:post
     (fn [request-map]
       (create-person personRepository idRepository request-map))
     :get
     (fn [_]
       (response-ok "persons" (PersonService/getAll personRepository)))}]
   ["/persons/:id"
    {:delete
     (fn [{{:keys [id]} :path-params}]
       (response-ok "person" (PersonService/deleteById personRepository id)))}
    ]])