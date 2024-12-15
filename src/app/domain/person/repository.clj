(ns app.domain.person.repository)

(defn create [save! get-all delete-by-id]
  {:save!        save!
   :get-all      get-all
   :delete-by-id delete-by-id})
