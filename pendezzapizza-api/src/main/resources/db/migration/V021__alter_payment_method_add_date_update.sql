alter table payment_method add update_date datetime null;
update payment_method set update_date = utc_timestamp;
alter table payment_method modify update_date datetime not null;

alter table permission add update_date datetime null;
update permission set update_date = utc_timestamp;
alter table permission modify update_date datetime not null;

alter table state add update_date datetime null;
update state set update_date = utc_timestamp;
alter table state modify update_date datetime not null;

alter table city add update_date datetime null;
update city set update_date = utc_timestamp;
alter table city modify update_date datetime not null;

alter table product add update_date datetime null;
update product set update_date = utc_timestamp;
alter table product modify update_date datetime not null;

alter table `group` add update_date datetime null;
update `group` set update_date = utc_timestamp;
alter table `group` modify update_date datetime not null;

alter table `order` add update_date datetime null;
update `order` set update_date = utc_timestamp;
alter table `order` modify update_date datetime not null;

alter table `user` add update_date datetime null;
update `user` set update_date = utc_timestamp;
alter table `user` modify update_date datetime not null;


alter table product_photo add update_date datetime null;
update product_photo set update_date = utc_timestamp;
alter table product_photo modify update_date datetime not null;