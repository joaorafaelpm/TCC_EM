create table IF NOT EXISTS product_photo (
    product_id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())),

    archive_name varchar(150) not null ,
    description varchar(150) not null ,
    content_type varchar(80) not null ,
    `size` int not null ,

    primary key (product_id),

    constraint fk_product_photo_product foreign key (product_id) references product(id)
) engine=InnoDB default charset=utf8;