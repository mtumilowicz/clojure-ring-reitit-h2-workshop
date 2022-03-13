-- :name create-person! :insert :raw
INSERT INTO person
(id, first_name, last_name)
VALUES (:id, :firstName, :lastName)

-- :name get-persons :? :*
SELECT * FROM person

-- :name delete-by-id! :! :n
DELETE FROM person WHERE id=:id