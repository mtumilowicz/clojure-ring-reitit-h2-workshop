(ns app.application
  (:require
    [app.domain.id.service :as IdService]
    [app.domain.person.service :as PersonService]
    [app.infrastructure.id.repository.deterministic :as DeterministicIdRepository]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.reload :refer [wrap-reload]]
    [app.gateway.api :as Api]
    [app.infrastructure.app.config :refer [env]]
    [luminus-migrations.core :as migrations]
    [mount.core :as mount]
    [clojure.tools.logging :as log]
    [app.infrastructure.person.module :as PersonModule]
    [app.infrastructure.id.module :as IdModule]))

(Thread/setDefaultUncaughtExceptionHandler
  (reify Thread$UncaughtExceptionHandler
    (uncaughtException [_ thread ex]
      (log/error {:what      :uncaught-exception
                  :exception ex
                  :where     (str "Uncaught exception on" (.getName thread))}))))

(def uuid-id-repository IdModule/uuidRepository) ;; Create repo once
(def id-service (IdService/create-id-service uuid-id-repository))
(def person-in-memory-repository (PersonModule/inMemoryRepository (atom {})))
(def person-service (PersonService/create-person-service person-in-memory-repository id-service))


(defn -main [& args]
  (mount/start #'app.infrastructure.db.config/*db*)
  (migrations/init (select-keys env [:database-url]))
  (jetty/run-jetty
    (-> (Api/handler person-service id-service))
    {:port (:port env)}))