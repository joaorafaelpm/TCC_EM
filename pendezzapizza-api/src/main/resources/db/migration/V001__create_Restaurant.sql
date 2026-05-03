CREATE TABLE IF NOT EXISTS restaurant (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    name VARCHAR(200) NOT NULL ,
    shipping_fee DECIMAL(9,2) NOT NULL,
    description VARCHAR(500) NULL ,
    average_delivery_time_minutes INT NULL ,
    minimum_order_value DECIMAL(10, 2) NULL
) engine=InnoDB default charset=utf8;

