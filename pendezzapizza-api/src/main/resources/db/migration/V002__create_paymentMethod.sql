CREATE TABLE IF NOT EXISTS payment_method (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    description VARCHAR(100) NOT NULL
) engine=InnoDB default charset=utf8;