DROP INDEX IF EXISTS idx_client_veterinarian_id;
DROP INDEX IF EXISTS idx_horses_client_id;
DROP INDEX IF EXISTS idx_consultation_horse_id;
DROP INDEX IF EXISTS idx_consultation_detail_consultation_id;
DROP INDEX IF EXISTS idx_consultation_detail_product_id;
DROP TABLE IF EXISTS ConsultationDetail;
DROP TABLE IF EXISTS Consultation;
DROP TABLE IF EXISTS Horses;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS ProductType;
DROP TABLE IF EXISTS Veterinarian;
DROP SEQUENCE IF EXISTS consultation_detail_seq;
DROP SEQUENCE IF EXISTS product_type_seq;
DROP SEQUENCE IF EXISTS consultation_seq;
DROP SEQUENCE IF EXISTS horse_seq;
DROP SEQUENCE IF EXISTS client_seq;
DROP SEQUENCE IF EXISTS veterinarian_seq;

CREATE SEQUENCE IF NOT EXISTS veterinarian_seq START 1;
CREATE TABLE Veterinarian
(
    id       BIGINT PRIMARY KEY DEFAULT nextval('veterinarian_seq'),
    username VARCHAR(50)  NOT NULL,
    password TEXT         NOT NULL,
    email    VARCHAR(100) NOT NULL,
    phone    VARCHAR(15)  NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS client_seq START 1;
CREATE TABLE Client
(
    id             BIGINT PRIMARY KEY DEFAULT nextval('client_seq'),
    name           VARCHAR(100)        NOT NULL,
    email          VARCHAR(100) UNIQUE NOT NULL,
    phone          VARCHAR(15)         NOT NULL,
    veterinarianID BIGINT              NOT NULL,
    FOREIGN KEY (veterinarianID)
        REFERENCES Veterinarian (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS horse_seq START 1;
CREATE TABLE Horses
(
    id        BIGINT PRIMARY KEY DEFAULT nextval('horse_seq'),
    horseName VARCHAR(100) NOT NULL,
    age       BIGINT       NOT NULL,
    clientID  BIGINT       NOT NULL,
    FOREIGN KEY (clientID)
        REFERENCES Client (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS consultation_seq START 1;
CREATE TABLE Consultation
(
    id        BIGINT PRIMARY KEY DEFAULT nextval('consultation_seq'),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    horseID   BIGINT                   NOT NULL,
    FOREIGN KEY (horseID)
        REFERENCES Horses (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS product_type_seq START 1;
CREATE TABLE ProductType
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('product_type_seq'),
    type VARCHAR(50) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS consultation_detail_seq START 1;
CREATE TABLE ConsultationDetail
(
    consultationID BIGINT NOT NULL,
    productID      BIGINT NOT NULL,
    quantity       INT    NOT NULL DEFAULT 1,
    PRIMARY KEY (consultationID, productID),
    FOREIGN KEY (consultationID)
        REFERENCES Consultation (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (productID)
        REFERENCES ProductType (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE INDEX idx_client_veterinarian_id ON Client (veterinarianID);
CREATE INDEX idx_horses_client_id ON Horses (clientID);
CREATE INDEX idx_consultation_horse_id ON Consultation (horseID);
CREATE INDEX idx_consultation_detail_consultation_id ON ConsultationDetail (consultationID);
CREATE INDEX idx_consultation_detail_product_id ON ConsultationDetail (productID);