CREATE TABLE grupo_permissao (
    grupo_id BINARY(16) NOT NULL,
    permissao_id BINARY(16) NOT NULL,

    PRIMARY KEY (grupo_id, permissao_id),

    CONSTRAINT fk_grupo FOREIGN KEY (grupo_id)
            REFERENCES grupo(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_permissao FOREIGN KEY (permissao_id)
            REFERENCES permissao(id)
            ON DELETE CASCADE
);


