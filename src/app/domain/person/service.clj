(ns app.domain.person.service
  (:require
    [app.domain.parser :as Parser]
    [app.domain.person.new-person-command :as NewPersonCommand]
    [app.domain.either :as Either]))

(defn assign-id [id-service new-person-command]
  (let [id ((:generate id-service))]
    (NewPersonCommand/assign-id id new-person-command)))

(defn save [person-repository id-service new-person-command]
  (->> new-person-command
       (Parser/parse NewPersonCommand/Schema)
       (Either/map #(assign-id id-service %))
       (Either/flat-map #((:save! person-repository) %))))

(defn get-all [person-repository]
  ((:get-all person-repository)))

(defn delete-by-id [person-repository id]
  ((:delete-by-id person-repository) id))

(defn mkService [person-repository id-service]
  {:assign-id (partial assign-id id-service)
   :save (partial save person-repository id-service)
   :get-all (partial get-all person-repository)
   :delete-by-id (partial delete-by-id person-repository)})