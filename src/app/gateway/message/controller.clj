(ns app.gateway.message.controller
  (:require [app.gateway.middleware :refer :all]
            [ring.util.http-response :as response]
            [app.domain.message.service :as MessageService]
            [app.domain.message.records :refer :all]
            [app.gateway.message.input :refer :all]
            [app.gateway.output :refer :all]
            [clojure.core.match :refer [match]]))

(defn toDomain [messageApiInput]
  (map->Message messageApiInput))

(defn saveMessage [messageRepo request-map]
  (match (parse-messageApiInput (:body-params request-map))
         [:left errors] (response/bad-request (failure errors))
         [:right valid]     (->> valid
                                   (toDomain)
                                   (MessageService/save messageRepo)
                                   (success "msg")
                                   (#(response/ok %)))))

(defn routes [{:keys [:messageRepo]}]
  ["/api" {:middleware [wrap-formats]}
   ["/messages"
    {:post
     (fn [request-map] (saveMessage messageRepo request-map))
     :get
     (fn [_]
       (response/ok {:result (MessageService/getAll messageRepo)}))
     :delete
     (fn [{{:keys [id]} :body-params}]
       (response/ok {:result (MessageService/deleteById messageRepo id)})
       )}]])