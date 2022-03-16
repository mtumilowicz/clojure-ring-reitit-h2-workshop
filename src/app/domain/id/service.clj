(ns app.domain.id.service
  (:require [app.domain.id.repository :as IdRepository]))

(defn generate [idRepository]
  (IdRepository/generate idRepository))