(ns app.domain.id.service)

(defn generate [id-repository]
  (:generate id-repository))

(defn mkService [id-repository]
  {:generate (generate id-repository)})