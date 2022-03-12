-- :name save-message! :! :n
-- :doc creates a new message using the name and message keys
INSERT INTO guestbook
(id, message)
VALUES (:id, :message)

-- :name get-messages :? :*
-- :doc selects all available messages
SELECT * FROM guestbook

-- :name delete-by-id! :! :n
-- :doc delete message using id
DELETE FROM guestbook WHERE id=:id