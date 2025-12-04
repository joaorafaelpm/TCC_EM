create table IF NOT EXISTS `order` (
	id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())),
    subtotal DECIMAL(9 ,2) NOT NULL ,
    shipping_fee DECIMAL(9 ,2) NOT NULL ,
    total_cost DECIMAL(9 ,2) NOT NULL ,

    creation_date DATETIME NOT NULL,
    confirmation_date DATETIME ,
    cancellation_date DATETIME ,
    delivery_date DATETIME ,

    status_order VARCHAR(10) NOT NULL DEFAULT "CREATED" ,
    payment_method_id BINARY(16) NOT NULL,
    restaurant_id BINARY(16) NOT NULL,
    client_user_id BINARY(16) NOT NULL,

    address_city_id BINARY(16) NOT NULL,
    address_street VARCHAR(255) NOT NULL,
    address_number VARCHAR(255) NOT NULL,
    address_complement VARCHAR(255),
    address_neighborhood VARCHAR(255) NOT NULL,

    constraint fk_order_address_city foreign key (address_city_id) references city (id),
    constraint fk_order_restaurant foreign key (restaurant_id) references restaurant (id),
    constraint fk_order_user_client foreign key (client_user_id) references user (id),
    constraint fk_order_payment_method foreign key (payment_method_id) references payment_method (id),

	primary key (id)
) engine=InnoDB default charset=utf8;
