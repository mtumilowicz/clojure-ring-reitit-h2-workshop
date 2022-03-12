(ns app.domain.message.repository)

(defprotocol MessageRepository
  (save! [this message])
  (getAll [this])
  (deleteById [this id]))