ALTER TABLE restaurant
    ADD COLUMN address_zip_code VARCHAR(255) NOT NULL,
    ADD COLUMN address_street VARCHAR(255) NOT NULL,
    ADD COLUMN address_number VARCHAR(255) NOT NULL,
    ADD COLUMN address_complement VARCHAR(255) ,
    ADD COLUMN address_neighborhood VARCHAR(255) NOT NULL,
    ADD COLUMN address_city_id BINARY(16) NOT NULL,
    ADD CONSTRAINT fk_address_city
        FOREIGN KEY (address_city_id)
        REFERENCES city(id);
