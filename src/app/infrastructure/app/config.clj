(ns app.infrastructure.app.config
  (:require
    [cprop.core :refer [load-config]]))

(def env (load-config))