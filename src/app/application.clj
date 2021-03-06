(ns app.application
  (:require
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

(def dependencies {:personRepository PersonModule/dbRepository
                   :idRepository     IdModule/uuidRepository})

(defn -main [& args]
  (mount/start #'app.infrastructure.db.config/*db*)
  (migrations/init (select-keys env [:database-url]))
  (jetty/run-jetty
    (-> (Api/handler dependencies))
    {:port (:port env)}))