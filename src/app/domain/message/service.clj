(ns app.domain.message.service
  (:require [app.domain.message.repository :as MessageRepository]))

(defn save [messageRepository message]
      (MessageRepository/save! messageRepository message))

(defn getAll [messageRepository]
  (MessageRepository/getAll messageRepository))

(defn deleteById [messageRepository id]
  (MessageRepository/deleteById messageRepository id))