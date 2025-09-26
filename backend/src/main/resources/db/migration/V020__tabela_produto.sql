CREATE TABLE produto (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL ,
    descricao VARCHAR(255) NOT NULL ,
    preco DECIMAL(9,2) NOT NULL ,
    restaurante_id BINARY(16)
);

ALTER TABLE produto
ADD CONSTRAINT fk_produto_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id);
