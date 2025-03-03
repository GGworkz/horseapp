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

CREATE SEQUENCE IF NOT EXISTS client_seq START 1;
CREATE TABLE clients
(
    id         BIGINT PRIMARY KEY DEFAULT NEXTVAL('client_seq'),
    first_name VARCHAR(50)         NOT NULL,
    last_name  VARCHAR(50)         NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    phone      VARCHAR(15)         NOT NULL,
    user_id    BIGINT              NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

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


CREATE SEQUENCE IF NOT EXISTS product_type_seq START 1;
CREATE TABLE product_types
(
    id   BIGINT PRIMARY KEY DEFAULT NEXTVAL('product_type_seq'),
    type VARCHAR(50) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS consultation_detail_seq START 1;
CREATE TABLE consultationdetail
(
    consultationid BIGINT NOT NULL,
    productid      BIGINT NOT NULL,
    quantity       INT    NOT NULL DEFAULT 1,
    PRIMARY KEY (consultationid, productid),
    FOREIGN KEY (consultationid)
        REFERENCES consultation (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (productid)
        REFERENCES producttype (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE INDEX idx_client_veterinarian_id ON client (veterinarianid);
CREATE INDEX idx_horses_client_id ON horses (clientid);
CREATE INDEX idx_consultation_horse_id ON consultation (horseid);
CREATE INDEX idx_consultation_detail_consultation_id ON consultationdetail (consultationid);
CREATE INDEX idx_consultation_detail_product_id ON consultationdetail (productid);