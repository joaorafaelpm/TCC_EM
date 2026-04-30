ALTER TABLE `user`
    ADD COLUMN phone VARCHAR(20) NULL AFTER email;

ALTER TABLE restaurant
    ADD COLUMN description VARCHAR(500) NULL AFTER shipping_fee,
    ADD COLUMN average_delivery_time_minutes INT NULL AFTER description,
    ADD COLUMN minimum_order_value DECIMAL(10, 2) NULL AFTER average_delivery_time_minutes;