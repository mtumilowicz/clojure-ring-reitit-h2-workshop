(ns app.itest
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [app.gateway.api :as Api]
            [muuntaja.core :as m]
            [app.infrastructure.message.module :as MessageModule]))

(def dependencies {:messageRepo MessageModule/inMemoryRepository})
(def app (Api/handler dependencies))

(deftest test-app
  (testing "response coercion error"
    (let [response (app (-> (request :post "/api/messages")
                              (json-body {:id 5, :message "bonjour"})))]
      (is (= 200 (:status response)))))
  (testing "main route"
    (let [response (app (request :get "/api/messages"))]
      (is (= 200 (:status response)))
      (is (= {:5 {:id      5
                  :message "bonjour"}} (:result (m/decode-response-body response)))))))
