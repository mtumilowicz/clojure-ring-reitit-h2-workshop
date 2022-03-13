(ns app.domain.person.repository)

(defprotocol PersonRepository
  (save! [this person])
  (getAll [this])
  (deleteById [this id]))