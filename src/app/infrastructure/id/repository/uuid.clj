(ns app.infrastructure.id.repository.uuid
  (:require
    [app.domain.id.repository :refer [IdRepository]])
  (:import (java.util UUID)))

(defrecord UuidIdRepository []
  IdRepository
  (generate [_]
    str (UUID/randomUUID)))

(defn create [] (UuidIdRepository.))