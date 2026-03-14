ALTER TABLE oauth2_registered_client ADD COLUMN post_logout_redirect_uris varchar(1000) DEFAULT NULL;
