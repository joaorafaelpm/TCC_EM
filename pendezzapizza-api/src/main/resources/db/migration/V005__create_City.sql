CREATE TABLE IF NOT EXISTS city (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    state_id BINARY(16) NOT NULL
) engine=InnoDB default charset=utf8;

ALTER TABLE city ADD CONSTRAINT fk_city FOREIGN KEY (state_id) REFERENCES state (id) ;
