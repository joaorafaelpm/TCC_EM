CREATE TABLE IF NOT EXISTS user_group (
    user_id BINARY(16) NOT NULL,
    group_id BINARY(16) NOT NULL,

    PRIMARY KEY (user_id , group_id),

    CONSTRAINT fk_user FOREIGN KEY (user_id)
            REFERENCES `user`(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_group_user FOREIGN KEY (group_id)
            REFERENCES `group`(id)
            ON DELETE CASCADE
) engine=InnoDB default charset=utf8;
