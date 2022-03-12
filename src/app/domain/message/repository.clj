(ns app.domain.message.repository)

(defprotocol MessageRepository
  (save! [this messageInput])
  (getAll [this]))