(ns app.gateway.person.controller
  (:require
    [app.domain.parser :as Parser]
    [app.gateway.middleware :refer [wrap-formats]]
    [app.domain.person.service :as PersonService]
    [app.gateway.person.input :as ApiInput]
    [app.gateway.output :refer :all]
    [clojure.core.match :refer [match]]
    [app.domain.either :as either]))

(defn create-person [personRepository idService request-map]
  (->> (Parser/parse (:body-params request-map) ApiInput/NewPersonApiInput)
       (either/map ApiInput/to-NewPersonCommand)
       (either/flat-map #(PersonService/save personRepository idService %))
       (from-domain-result "person")))

(defn routes [{:keys [:personRepository :idRepository]}]
  ["/api" {:middleware [wrap-formats]}
   ["/persons"
    {:post
     (fn [request-map]
       (create-person personRepository idRepository request-map)
       )
     :get
     (fn [_]
       (response-ok "persons" (PersonService/getAll personRepository)))}]
   ["/persons/:id"
    {:delete
     (fn [{{:keys [id]} :path-params}]
       (response-ok "person" (PersonService/deleteById personRepository id)))}
    ]])