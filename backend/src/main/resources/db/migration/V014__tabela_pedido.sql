create table IF NOT EXISTS pedido (
	id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    subTotal DECIMAL(9 ,2) NOT NULL ,
    taxaFrete DECIMAL(9 ,2) NOT NULL ,
    valorTotal DECIMAL(9 ,2) NOT NULL ,

    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_confirmacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_cancelamento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_entrega DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    status_pedido VARCHAR(10) NOT NULL DEFAULT "CRIADO" ,
    forma_pagamento_id BINARY(16) NOT NULL ,
    restaurante_id BINARY(16) NOT NULL ,
    cliente_usuario_id BINARY(16) NOT NULL ,

    endereco_cidade_id BINARY(16) NOT NULL,
    endereco_logradouro VARCHAR(255) NOT NULL,
    endereco_numero VARCHAR(255) NOT NULL,
    endereco_complemento VARCHAR(255),
    endereco_bairro VARCHAR(255) NOT NULL,

    constraint fk_pedido_endereco_cidade foreign key (endereco_cidade_id) references cidade (id),
    constraint fk_pedido_restaurante foreign key (restaurante_id) references restaurante (id),
    constraint fk_pedido_usuario_cliente foreign key (cliente_usuario_id) references usuario (id),
    constraint fk_pedido_forma_pagamento foreign key (forma_pagamento_id) references forma_pagamento (id)

) engine=InnoDB default charset=utf8;
