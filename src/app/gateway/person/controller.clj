(ns app.gateway.person.controller
  (:require
            [app.domain.parser :refer :all]
            [app.gateway.middleware :refer :all]
            [app.domain.person.service :as PersonService]
            [app.gateway.person.input :refer :all]
            [app.gateway.output :refer :all]
            [clojure.core.match :refer [match]]
            [app.domain.either :refer [fold-either]]))

(defn create-person [personRepository request-map]
  (fold-either (parse (:body-params request-map) CreatePersonApiInput)
               bad-request
               #(->> %
                    (to-CreatePersonCommand)
                    (PersonService/save personRepository)
                    (from-domain-result))))

(defn routes [{:keys [:personRepository]}]
  ["/api" {:middleware [wrap-formats]}
   ["/persons"
    {:post
     (fn [request-map] (create-person personRepository request-map))
     :get
     (fn [_]
       (response-ok {:key "persons" :data (PersonService/getAll personRepository)}))
     :delete
     (fn [{{:keys [id]} :body-params}]
       (response-ok {:key "persons" :data (PersonService/deleteById personRepository id)})
       )}]])