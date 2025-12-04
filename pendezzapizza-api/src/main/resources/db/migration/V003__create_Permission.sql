CREATE TABLE IF NOT EXISTS permission (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    name VARCHAR(200) NOT NULL ,
    description VARCHAR(200) NOT NULL
) engine=InnoDB default charset=utf8;