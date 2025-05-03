-- Drop indexes
DROP INDEX IF EXISTS idx_users_username;
DROP INDEX IF EXISTS idx_users_email;
DROP INDEX IF EXISTS idx_users_phone;
DROP INDEX IF EXISTS idx_customers_email;
DROP INDEX IF EXISTS idx_customers_phone;
DROP INDEX IF EXISTS idx_consultations_timestamp;
DROP INDEX IF EXISTS idx_product_catalogs_type;
DROP INDEX IF EXISTS idx_product_catalogs_name;
DROP INDEX IF EXISTS idx_horses_name;
DROP INDEX IF EXISTS idx_customer_user_customer_id;
DROP INDEX IF EXISTS idx_customer_user_user_id;
DROP INDEX IF EXISTS idx_horses_customer_id;
DROP INDEX IF EXISTS idx_consultations_horse_id;
DROP INDEX IF EXISTS idx_product_catalogs_user_id;
DROP INDEX IF EXISTS idx_consultation_details_consultation_id;
DROP INDEX IF EXISTS idx_consultation_details_product_id;

-- Drop tables
DROP TABLE IF EXISTS consultation_details;
DROP TABLE IF EXISTS consultations;
DROP TABLE IF EXISTS horses;
DROP TABLE IF EXISTS customer_user;
DROP TABLE IF EXISTS product_catalogs;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS customers;

-- Drop sequences
DROP SEQUENCE IF EXISTS consultation_seq;
DROP SEQUENCE IF EXISTS horse_seq;
DROP SEQUENCE IF EXISTS customer_seq;
DROP SEQUENCE IF EXISTS product_catalog_seq;
DROP SEQUENCE IF EXISTS user_seq;

-- Users
CREATE SEQUENCE IF NOT EXISTS user_seq START 1;
CREATE TABLE users
(
    id         BIGINT PRIMARY KEY DEFAULT NEXTVAL('user_seq'),
    username   VARCHAR(50)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    phone      VARCHAR(15)  NOT NULL,
    CONSTRAINT unique_users_username UNIQUE(username),
    CONSTRAINT unique_users_email UNIQUE(email)
);

-- Product Catalogs
CREATE SEQUENCE IF NOT EXISTS product_catalog_seq START 1;
CREATE TABLE product_catalogs (
    id       BIGINT PRIMARY KEY DEFAULT NEXTVAL('product_catalog_seq'),
    name     VARCHAR(50)    NOT NULL,
    type     VARCHAR(20)    NOT NULL,
    price    NUMERIC(10, 2) NOT NULL,
    user_id  BIGINT         NOT NULL,
    FOREIGN KEY (user_id)
             REFERENCES users (id)
             ON DELETE CASCADE
             ON UPDATE CASCADE
);


-- Customers
CREATE SEQUENCE IF NOT EXISTS customer_seq START 1;
CREATE TABLE customers (
    id         BIGINT PRIMARY KEY DEFAULT NEXTVAL('customer_seq'),
    username   VARCHAR(50)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    phone      VARCHAR(15)  NOT NULL,
    CONSTRAINT unique_customers_username UNIQUE(username),
    CONSTRAINT unique_customers_email UNIQUE(email)
);

-- Client Users
CREATE TABLE customer_user (
    customer_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (customer_id, user_id),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Horses
CREATE SEQUENCE IF NOT EXISTS horse_seq START 1;
CREATE TABLE horses
(
    id          BIGINT PRIMARY KEY DEFAULT NEXTVAL('horse_seq'),
    name        VARCHAR(50) NOT NULL,
    age         BIGINT      NOT NULL,
    customer_id BIGINT      NOT NULL,
    FOREIGN KEY (customer_id)
        REFERENCES customers (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Consultations
CREATE SEQUENCE IF NOT EXISTS consultation_seq START 1;
CREATE TABLE consultations
(
    id        BIGINT PRIMARY KEY DEFAULT NEXTVAL('consultation_seq'),
    horse_id  BIGINT                   NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (horse_id)
        REFERENCES horses (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Consultation Details
CREATE TABLE consultation_details (
      consultation_id BIGINT NOT NULL,
      horse_id        BIGINT NOT NULL,
      product_id      BIGINT NOT NULL,
      user_id         BIGINT NOT NULL,
      quantity        INT    NOT NULL DEFAULT 1,
      PRIMARY KEY (consultation_id, product_id),
      FOREIGN KEY (consultation_id)
          REFERENCES consultations (id)
          ON DELETE CASCADE
          ON UPDATE CASCADE,
      FOREIGN KEY (product_id)
          REFERENCES product_catalogs (id)
          ON DELETE CASCADE
          ON UPDATE CASCADE
);

-- Commonly looked up fields
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_phone ON users (phone);
CREATE INDEX idx_customers_email ON customers (email);
CREATE INDEX idx_customers_phone ON customers (phone);
CREATE INDEX idx_consultations_timestamp ON consultations (timestamp);
CREATE INDEX idx_product_catalogs_type ON product_catalogs (type);
CREATE INDEX idx_product_catalogs_name ON product_catalogs (name);
CREATE INDEX idx_horses_name ON horses (name);

-- Foreign keys indexing to improve JOINS
CREATE INDEX idx_customer_user_customer_id ON customer_user (customer_id);
CREATE INDEX idx_customer_user_user_id ON customer_user (user_id);
CREATE INDEX idx_horses_customer_id ON horses (customer_id);
CREATE INDEX idx_consultations_horse_id ON consultations (horse_id);
CREATE INDEX idx_product_catalogs_user_id ON product_catalogs (user_id);
CREATE INDEX idx_consultation_details_consultation_id ON consultation_details (consultation_id);
CREATE INDEX idx_consultation_details_product_id ON consultation_details (product_id);
