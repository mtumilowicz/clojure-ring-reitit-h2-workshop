(ns app.person-endpoint-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [app.gateway.api :as Api]
            [muuntaja.core :as m]
            [app.infrastructure.person.module :as PersonModule]
            [app.infrastructure.id.module :as IdModule]))

(def dependencies {:personRepository (PersonModule/inMemoryRepository (atom {}))
                   :idRepository     IdModule/deterministicRepository})
(def app (Api/handler (:personRepository dependencies)
                      (:idRepository dependencies)))
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
      (is (= expectedResponse (-> (m/decode-response-body response) :data :persons))))))
