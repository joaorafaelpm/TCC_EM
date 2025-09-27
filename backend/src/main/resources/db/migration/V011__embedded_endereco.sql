ALTER TABLE restaurante
    ADD COLUMN endereco_cep VARCHAR(255),
    ADD COLUMN endereco_logradouro VARCHAR(255),
    ADD COLUMN endereco_numero VARCHAR(255),
    ADD COLUMN endereco_complemento VARCHAR(255),
    ADD COLUMN endereco_bairro VARCHAR(255),
    ADD COLUMN endereco_cidade_id BINARY(16) NOT NULL,
    
    ADD CONSTRAINT fk_endereco_cidade
        FOREIGN KEY (endereco_cidade_id)
        REFERENCES cidade(id);
