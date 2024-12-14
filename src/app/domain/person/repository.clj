(ns app.domain.person.repository)

(defprotocol PersonRepository
  (save! [this person])
  (getAll [this])
  (deleteById [this id]))

(defn create [save! get-all delete-by-id]
  {:save! save!
   :get-all get-all
   :delete-by-id delete-by-id})
