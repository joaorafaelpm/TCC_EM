create table IF NOT EXISTS restaurant_photo (
    restaurant_id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())),

    archive_name varchar(150) not null ,
    description varchar(150) not null ,
    content_type varchar(80) not null ,
    `size` int not null ,

    primary key (restaurant_id),

    constraint fk_restaurant_photo_restaurant foreign key (restaurant_id) references restaurant(id)
) engine=InnoDB default charset=utf8;