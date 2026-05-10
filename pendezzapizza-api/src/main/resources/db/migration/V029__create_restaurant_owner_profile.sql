CREATE TABLE restaurant_owner_profile (
    id                  BINARY(16)   DEFAULT (UUID_TO_BIN(UUID())),
    user_id             BINARY(16)   NOT NULL,
    cpf                 VARCHAR(255) NOT NULL,
    verified_at         DATETIME     NULL,

    CONSTRAINT pk_restaurant_owner_profile PRIMARY KEY (id),
    CONSTRAINT fk_restaurant_owner_profile_user
        FOREIGN KEY (user_id) REFERENCES `user` (id)
        ON DELETE CASCADE
);