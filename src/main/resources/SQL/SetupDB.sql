DROP SEQUENCE person_id_seq;
DROP TABLE Person;
DROP SEQUENCE belongings_id_seq;
DROP TABLE PersonBelongings;

-- Sequence for Person table
CREATE SEQUENCE person_id_seq START 1;
CREATE TABLE Person
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('person_id_seq'),
    name VARCHAR(100) NOT NULL,
    age  INT          NOT NULL
);

-- Sequence for PersonBelongings table
CREATE SEQUENCE belongings_id_seq START 1;
CREATE TABLE PersonBelongings
(
    id        BIGINT PRIMARY KEY DEFAULT nextval('belongings_id_seq'),
    person_id BIGINT       NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    FOREIGN KEY (person_id) REFERENCES Person (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

