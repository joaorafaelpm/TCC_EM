CREATE TABLE IF NOT EXISTS usuario_grupo (
    usuario_id BINARY(16) NOT NULL,
    grupo_id BINARY(16) NOT NULL,

    PRIMARY KEY (usuario_id , grupo_id),

    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id)
            REFERENCES usuario(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_grupo_usuario FOREIGN KEY (grupo_id)
            REFERENCES grupo(id)
            ON DELETE CASCADE
) engine=InnoDB default charset=utf8;
