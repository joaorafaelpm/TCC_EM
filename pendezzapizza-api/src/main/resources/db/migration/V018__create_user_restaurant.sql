CREATE TABLE IF NOT EXISTS restaurant_user_responsible (
    user_id BINARY(16) NOT NULL ,
    restaurant_id BINARY(16) NOT NULL,

    PRIMARY KEY (user_id , restaurant_id),

    CONSTRAINT fk_restaurant_user_user FOREIGN KEY (user_id)
            REFERENCES user(id),
    CONSTRAINT fk_restaurant_user_restaurant FOREIGN KEY (restaurant_id)
            REFERENCES restaurant(id)
) engine=InnoDB default charset=utf8;
