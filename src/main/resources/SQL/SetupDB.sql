-- Users
CREATE SEQUENCE IF NOT EXISTS user_seq START 1;
CREATE TABLE users
(
    id         BIGINT PRIMARY KEY DEFAULT NEXTVAL('user_seq'),
    username   VARCHAR(50)  NOT NULL,
    password   TEXT         NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    phone      VARCHAR(15)  NOT NULL
);

-- Clients
CREATE SEQUENCE IF NOT EXISTS client_seq START 1;
CREATE TABLE clients
(
    id         BIGINT PRIMARY KEY DEFAULT NEXTVAL('client_seq'),
    username   VARCHAR(50)  NOT NULL,
    password   TEXT         NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    phone      VARCHAR(15)  NOT NULL
);

-- Client Users
CREATE TABLE client_user (
    client_id INT,
    user_id INT,
    PRIMARY KEY (client_id, user_id),
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Horses
CREATE SEQUENCE IF NOT EXISTS horse_seq START 1;
CREATE TABLE horses
(
    id        BIGINT PRIMARY KEY DEFAULT NEXTVAL('horse_seq'),
    name      VARCHAR(50) NOT NULL,
    age       BIGINT      NOT NULL,
    client_id BIGINT      NOT NULL,
    FOREIGN KEY (client_id)
        REFERENCES clients (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Consultations
CREATE SEQUENCE IF NOT EXISTS consultation_seq START 1;
CREATE TABLE consultations
(
    id        BIGINT DEFAULT NEXTVAL('consultation_seq'),
    horse_id  BIGINT                   NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (id, horse_id),
    FOREIGN KEY (horse_id)
        REFERENCES horses (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Product Catalogs
CREATE SEQUENCE IF NOT EXISTS product_catalog_seq START 1;
CREATE TABLE product_catalogs
(
    id        BIGINT DEFAULT NEXTVAL('product_catalog_seq'),
    name      VARCHAR(50)    NOT NULL,
    type      VARCHAR(20)    NOT NULL,
    price     NUMERIC(10, 2) NOT NULL,
    client_id BIGINT         NOT NULL,
    PRIMARY KEY (id, client_id),
    FOREIGN KEY (client_id)
        REFERENCES clients (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Consultation Details
CREATE TABLE consultation_details
(
    consultation_id BIGINT NOT NULL,
    horse_id        BIGINT NOT NULL,
    product_id      BIGINT NOT NULL,
    client_id       BIGINT NOT NULL,
    quantity        INT    NOT NULL DEFAULT 1,
    PRIMARY KEY (consultation_id, horse_id, product_id, client_id),
    FOREIGN KEY (consultation_id, horse_id)
        REFERENCES consultations (id, horse_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (product_id, client_id)
        REFERENCES product_catalogs (id, client_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Commonly looked up fields
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_phone ON users (phone);
CREATE INDEX idx_clients_email ON clients (email);
CREATE INDEX idx_clients_phone ON clients (phone);
CREATE INDEX idx_consultations_timestamp ON consultations (timestamp);
CREATE INDEX idx_product_catalogs_type ON product_catalogs (type);

-- Foreign keys indexing to improve JOINS
CREATE INDEX idx_clients_user_id ON clients (user_id);
CREATE INDEX idx_horses_client_id ON horses (client_id);
CREATE INDEX idx_consultations_horse_id ON consultations (horse_id);
CREATE INDEX idx_product_catalogs_client_id ON product_catalogs (client_id);
CREATE INDEX idx_consultation_details_consultation_id ON consultation_details (consultation_id);
CREATE INDEX idx_consultation_details_horse_id ON consultation_details (horse_id);
CREATE INDEX idx_consultation_details_product_id ON consultation_details (product_id);
CREATE INDEX idx_consultation_details_client_id ON consultation_details (client_id);
