(ns app.itest
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [app.gateway.api :as Api]
            [muuntaja.core :as m]
            [app.infrastructure.message.module :as MessageModule]))

(def dependencies {:messageRepo MessageModule/inMemoryRepository})
(def app (Api/handler dependencies))

(deftest test-app
  (testing "create"
    (let [response (app (-> (request :post "/api/messages")
                              (json-body {:id 5, :message "bonjour"})))]
      (is (= 200 (:status response)))))
  (testing "get"
    (let [response (app (request :get "/api/messages"))]
      (is (= 200 (:status response)))
      (is (= {:5 {:id      5
                  :message "bonjour"}} (-> (m/decode-response-body response) :data :messages )))))
  (testing "delete"
    (let [response (app (-> (request :delete "/api/messages")
                            (json-body {:id 5})))]
      (is (= 200 (:status response)))))
  (testing "get"
    (let [response (app (request :get "/api/messages"))]
      (is (= 200 (:status response)))
      (is (= {} (-> (m/decode-response-body response) :data :messages)))))
  )
