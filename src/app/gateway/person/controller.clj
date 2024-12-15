(ns app.gateway.person.controller
  (:require
    [app.domain.either :as either]
    [app.domain.parser :as Parser]
    [app.gateway.output :refer :all]
    [app.gateway.person.input :as ApiInput]))

(defn create-person [person-service request-map]
  (->> request-map
       (:body-params)
       (Parser/parse ApiInput/NewPersonApiInputSchema)
       (either/map ApiInput/to-NewPersonCommand)
       (either/flat-map #((:save person-service) %))
       (from-domain-result "person")))

(defn routes [person-service]
  ["/api"
   ["/persons"
    {:post
     (fn [request-map]
       (create-person person-service request-map))
     :get
     (fn [_]
       (response-ok "persons" ((:get-all person-service))))}]
   ["/persons/:id"
    {:delete
     (fn [{{:keys [id]} :path-params}]
       (response-ok "person" ((:delete-by-id person-service) id)))}
    ]])