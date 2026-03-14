CREATE TABLE IF NOT EXISTS `group` (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) engine=InnoDB default charset=utf8;

