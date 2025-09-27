create table IF NOT EXISTS item_pedido (
	id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
	quantidade INTEGER(9) not null ,
    precoUnitario DECIMAL(9 ,2) NOT NULL ,
    precoTotal DECIMAL(9 ,2) NOT NULL ,
    observacao VARCHAR(255),
    pedido_id BINARY(16) NOT NULL ,
    produto_id BINARY(16) NOT NULL ,

    constraint fk_pedido_id FOREIGN KEY (pedido_id) references pedido (id),
    constraint fk_produto_id FOREIGN KEY (produto_id) references produto (id),

    unique key uk_item_pedido_produto (pedido_id, produto_id)

) engine=InnoDB default charset=utf8;
