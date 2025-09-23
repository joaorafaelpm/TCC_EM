CREATE TABLE grupo_permissao (
    grupo_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,

    PRIMARY KEY (grupo_id, permissao_id),

    CONSTRAINT fk_grupo FOREIGN KEY (grupo_id)
            REFERENCES grupo(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_permissao FOREIGN KEY (permissao_id)
            REFERENCES permissao(id)
            ON DELETE CASCADE
);


