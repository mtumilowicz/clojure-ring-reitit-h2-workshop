(ns app.gateway.message.controller
  (:require [app.gateway.middleware :refer :all]
            [app.domain.message.service :as MessageService]
            [app.gateway.message.input :refer :all]
            [app.gateway.output :refer :all]
            [clojure.core.match :refer [match]]
            [ring.util.http-response :as response]))

(defn saveMessage [messageRepo request-map]
  (match (parse-CreateMessageApiInput (:body-params request-map))
         [:left errors] (bad-request errors)
         [:right valid] (->> valid
                             (MessageService/save messageRepo)
                             (#(response-ok {:key "message" :data %})))))

(defn routes [{:keys [:messageRepo]}]
  ["/api" {:middleware [wrap-formats]}
   ["/messages"
    {:post
     (fn [request-map] (saveMessage messageRepo request-map))
     :get
     (fn [_]
       (response-ok {:key "messages" :data (MessageService/getAll messageRepo)}))
     :delete
     (fn [{{:keys [id]} :body-params}]
       (response-ok {:key "messages" :data (MessageService/deleteById messageRepo id)})
       )}]])