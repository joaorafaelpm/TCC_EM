CREATE TABLE ingredient (
    id                  BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    restaurant_id       BINARY(16)   NOT NULL,
    name                VARCHAR(120) NOT NULL,
    unit                VARCHAR(10)  NOT NULL,
    stock_quantity      BIGINT       NOT NULL DEFAULT 0,
    low_stock_threshold BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT fk_ingredient_restaurant
        FOREIGN KEY (restaurant_id) REFERENCES restaurant (id),
    CONSTRAINT uk_ingredient_restaurant_name UNIQUE (restaurant_id, name),
    CONSTRAINT ck_ingredient_unit
        CHECK (unit IN ('G', 'ML', 'UN')),
    CONSTRAINT ck_ingredient_stock_non_negative
        CHECK (stock_quantity >= 0),
    CONSTRAINT ck_ingredient_threshold_non_negative
        CHECK (low_stock_threshold >= 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE recipe_item (
    id                BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    product_id        BINARY(16) NOT NULL,
    ingredient_id     BINARY(16) NOT NULL,
    quantity_per_unit BIGINT NOT NULL,

    CONSTRAINT fk_recipe_item_product
        FOREIGN KEY (product_id) REFERENCES product (id),
    CONSTRAINT fk_recipe_item_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredient (id),
    CONSTRAINT uk_recipe_item_product_ingredient
        UNIQUE (product_id, ingredient_id),
    CONSTRAINT ck_recipe_item_quantity_positive
        CHECK (quantity_per_unit > 0)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE ingredient_movement (
    id            BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,
    ingredient_id BINARY(16) NOT NULL,
    type          VARCHAR(20) NOT NULL,
    quantity      BIGINT      NOT NULL,
    order_id      BINARY(16)      NULL,
    created_at    DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    CONSTRAINT fk_ingredient_movement_ingredient
        FOREIGN KEY (ingredient_id) REFERENCES ingredient (id),
    CONSTRAINT fk_ingredient_movement_order
        FOREIGN KEY (order_id) REFERENCES `order` (id),
    CONSTRAINT ck_ingredient_movement_type
        CHECK (type IN ('ENTRADA', 'SAIDA_PEDIDO', 'ESTORNO', 'AJUSTE')),
    CONSTRAINT ck_ingredient_movement_quantity
        CHECK ((type = 'AJUSTE' AND quantity <> 0)
            OR (type <> 'AJUSTE' AND quantity > 0)),
    CONSTRAINT ck_ingredient_movement_order_link
        CHECK ((type IN ('SAIDA_PEDIDO', 'ESTORNO') AND order_id IS NOT NULL)
            OR (type IN ('ENTRADA', 'AJUSTE') AND order_id IS NULL))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE INDEX idx_ingredient_movement_ingredient_created
    ON ingredient_movement (ingredient_id, created_at);