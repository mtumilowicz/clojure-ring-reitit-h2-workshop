(ns app.gateway.output)

(defrecord ApiOutput [status data])

(defn success [name data]
  (map->ApiOutput {:status "success" :data {(keyword name) data}}))

(defn failure [failures]
  (map->ApiOutput {:status "fail" :data {:errors failures}}))