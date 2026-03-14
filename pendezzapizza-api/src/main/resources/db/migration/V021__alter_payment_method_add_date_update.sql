alter table payment_method add update_date datetime null;
update payment_method set update_date = utc_timestamp;
alter table payment_method modify update_date datetime not null;