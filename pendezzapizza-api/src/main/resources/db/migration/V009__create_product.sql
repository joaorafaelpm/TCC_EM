create table IF NOT EXISTS product (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
	restaurant_id BINARY(16) not null,
	name varchar(100) not null,
	description VARCHAR(255),
	price decimal(10,2) not null,
	active tinyint(1) not null

) engine=InnoDB default charset=utf8;

ALTER TABLE product
ADD CONSTRAINT fk_product_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id);