-- CREATE SEQUENCE orders_seq
--     START WITH 1 INCREMENT 50;
-- 
-- CREATE SEQUENCE payment_seq
--     START WITH 1 INCREMENT 50;

CREATE TABLE orders(
                       id serial NOT NULL PRIMARY KEY,
                       date TIMESTAMP NOT NULL,
                       amount DECIMAL NOT NULL,
                       paid BOOLEAN NOT NULL
);

CREATE TABLE payments(
                        id serial NOT NULL PRIMARY KEY,
                        order_id BIGINT NOT NULL UNIQUE,
                        credit_card_number VARCHAR(16) NOT NULL,
                        CONSTRAINT fk_order FOREIGN KEY(order_id) REFERENCES orders(id)
);