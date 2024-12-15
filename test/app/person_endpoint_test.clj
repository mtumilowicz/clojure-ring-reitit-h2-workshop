(ns app.person-endpoint-test
  (:require [app.domain.id.service :as IdService]
            [app.domain.person.service :as PersonService]
            [app.gateway.api :as Api]
            [app.infrastructure.id.module :as IdModule]
            [app.infrastructure.person.module :as PersonModule]
            [clojure.test :refer :all]
            [muuntaja.core :as m]
            [ring.mock.request :refer :all]))

(def deterministic-id-repository IdModule/deterministicRepository)
(def id-service (IdService/create-service deterministic-id-repository))
(def person-in-memory-repository PersonModule/inMemoryRepository)
(def person-service (PersonService/mkService person-in-memory-repository id-service))

(def dependencies {:person-service person-service})
(def app (Api/handler dependencies))
(def root "/api/persons")

(deftest test-app
  (testing "create"
    (let [response (app (-> (request :post root)
                            (json-body {:first_name "Michu" :last_name "Tichu"})))
          expectedResponse {:id        "1"
                            :firstName "Michu"
                            :lastName  "Tichu"}]
      (is (= 200 (:status response)))
      (is (= expectedResponse (-> (m/decode-response-body response) :data :person)))))
  (testing "get"
    (let [response (app (request :get root))
          expectedResponse [{:id        "1"
                             :firstName "Michu"
                             :lastName  "Tichu"}]]
      (is (= 200 (:status response)))
      (is (= expectedResponse (-> (m/decode-response-body response) :data :persons)))))
  (testing "delete"
    (let [response (app (-> (request :delete (str root "/1"))))
          expectedResponse {:id "1"}]
      (is (= 200 (:status response)))
      (is (= expectedResponse (-> (m/decode-response-body response) :data :person)))))
  (testing "get"
    (let [response (app (request :get root))
          expectedResponse []]
      (is (= 200 (:status response)))
      (is (= expectedResponse (-> (m/decode-response-body response) :data :persons)))))
  (let [response (app (-> (request :post root)
                          (json-body {:first_name "Michu" :last_name "Tichu"})))
        expectedResponse {:id        "2"
                          :firstName "Michu"
                          :lastName  "Tichu"}]
    (is (= 200 (:status response)))
    (is (= expectedResponse (-> (m/decode-response-body response) :data :person))))
  (let [response (app (-> (request :post root)
                          (json-body {:first_name "Michu" :last_name "Tichu"})))
        expectedResponse {:id        "3"
                          :firstName "Michu"
                          :lastName  "Tichu"}]
    (is (= 200 (:status response)))
    (is (= expectedResponse (-> (m/decode-response-body response) :data :person))))
  )
