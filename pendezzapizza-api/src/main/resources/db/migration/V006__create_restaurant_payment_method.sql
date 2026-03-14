CREATE TABLE IF NOT EXISTS restaurant_payment_method (
    restaurant_id  BINARY(16) NOT NULL,
    payment_method_id BINARY(16) NOT NULL,

    PRIMARY KEY (restaurant_id, payment_method_id),
    CONSTRAINT fk_restaurant FOREIGN KEY (restaurant_id)
        REFERENCES restaurant(id),
    CONSTRAINT fk_payment_method FOREIGN KEY (payment_method_id)
        REFERENCES payment_method(id)
) engine=InnoDB default charset=utf8;
