(ns app.domain.id.service)

(defn generate [id-repository]
  (:generate id-repository))

(defn create-service [id-repository]
  {:generate (generate id-repository)})