(ns app.domain.id.service
  (:require [app.domain.id.repository :as IdRepository]))

(defn generate [{:keys [idRepository]}]
  (IdRepository/generate idRepository))