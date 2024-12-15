(ns app.infrastructure.id.repository.uuid
  (:require
    [app.domain.id.repository :as id-repository])
  (:import (java.util UUID)))

(defn generate []
  (str (UUID/randomUUID)))

(defn create []
  (id-repository/create generate))