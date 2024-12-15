(ns app.application
  (:require
    [app.domain.id.service :as IdService]
    [app.domain.person.service :as PersonService]
    [app.gateway.api :as Api]
    [app.infrastructure.app.config :refer [env]]
    [app.infrastructure.id.module :as IdModule]
    [app.infrastructure.person.module :as PersonModule]
    [clojure.tools.logging :as log]
    [luminus-migrations.core :as migrations]
    [mount.core :as mount]
    [ring.adapter.jetty :as jetty]))

(Thread/setDefaultUncaughtExceptionHandler
  (reify Thread$UncaughtExceptionHandler
    (uncaughtException [_ thread ex]
      (log/error {:what      :uncaught-exception
                  :exception ex
                  :where     (str "Uncaught exception on" (.getName thread))}))))

(def uuid-id-repository IdModule/uuidRepository)
(def id-service (IdService/create-service uuid-id-repository))
(def person-db-repository PersonModule/dbRepository)
(def person-service (PersonService/mkService person-db-repository id-service))
(def services
  {:id-service id-service
   :person-service person-service})

(defn -main [& args]
  (mount/start #'app.infrastructure.db.config/*db*)
  (migrations/init (select-keys env [:database-url]))
  (jetty/run-jetty
    (-> (Api/handler services))
    {:port (:port env)}))