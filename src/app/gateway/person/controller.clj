(ns app.gateway.person.controller
  (:require [app.gateway.middleware :refer :all]
            [app.domain.person.service :as PersonService]
            [app.gateway.person.input :refer :all]
            [app.gateway.output :refer :all]
            [clojure.core.match :refer [match]]))

(defn create-person [personRepository request-map]
  (match (parse-CreatePersonApiInput (:body-params request-map))
         [:left errors] (bad-request errors)
         [:right valid] (->> valid
                             (PersonService/save personRepository)
                             (#(response-ok {:key "persons" :data %})))))

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