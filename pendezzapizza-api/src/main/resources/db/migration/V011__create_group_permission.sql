CREATE TABLE IF NOT EXISTS group_permission (
    group_id BINARY(16) NOT NULL,
    permission_id BINARY(16) NOT NULL,

    PRIMARY KEY (group_id, permission_id),

    CONSTRAINT fk_group FOREIGN KEY (group_id)
        REFERENCES `group`(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_permission FOREIGN KEY (permission_id)
        REFERENCES permission(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
