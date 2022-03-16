(ns app.domain.id.repository)

(defprotocol IdRepository
  (generate [this]))
