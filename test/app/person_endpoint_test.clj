(ns app.person-endpoint-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [app.gateway.api :as Api]
            [muuntaja.core :as m]
            [app.infrastructure.person.module :as PersonModule]))

(def dependencies {:personRepository PersonModule/inMemoryRepository})
(def app (Api/handler dependencies))

(deftest test-app
  (testing "create"
    (let [response (app (-> (request :post "/api/persons")
                              (json-body {:id 5 :first_name "Michu" :last_name "Tichu"})))]
      (is (= 200 (:status response)))))
  (testing "get"
    (let [response (app (request :get "/api/persons"))
          expectedResponse {:5 {:id      5
                                :firstName "Michu"
                                :lastName "Tichu"}}]
      (is (= 200 (:status response)))
      (is (= expectedResponse (-> (m/decode-response-body response) :data :persons )))))
  (testing "delete"
    (let [response (app (-> (request :delete "/api/persons")
                            (json-body {:id 5})))]
      (is (= 200 (:status response)))))
  (testing "get"
    (let [response (app (request :get "/api/persons"))
          expectedResponse {}]
      (is (= 200 (:status response)))
      (is (= expectedResponse (-> (m/decode-response-body response) :data :persons)))))
  )
