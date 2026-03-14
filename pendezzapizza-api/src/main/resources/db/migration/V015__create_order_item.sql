create table IF NOT EXISTS order_item (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())),
	quantity INTEGER(9) not null ,
    unity_price DECIMAL(9 ,2) NOT NULL ,
    total_price DECIMAL(9 ,2) NOT NULL ,
    observation VARCHAR(255),
    order_id BINARY(16) NOT NULL ,
    product_id BINARY(16) NOT NULL ,
    constraint fk_order_id FOREIGN KEY (order_id) references `order` (id),
    constraint fk_product_id FOREIGN KEY (product_id) references product (id),

    unique key uk_order_item_product (order_id, product_id),

	primary key (id)

) engine=InnoDB default charset=utf8;
