

DROP TABLE if exists country;
CREATE TABLE country (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `country_name` varchar(64) NOT NULL,
 `create_time` TIMESTAMP NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE INDEX `unique_country` (country_name)
);

DROP TABLE if exists language;
CREATE TABLE language (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `language_name` varchar(64) NOT NULL,
 `create_time` TIMESTAMP NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE INDEX `unique_language` (language_name)
);

DROP TABLE if exists third_channel;
CREATE TABLE third_channel (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `channel_name` varchar(64) NOT NULL,
 `create_time` TIMESTAMP NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE INDEX `unique_channel` (channel_name)
);

DROP TABLE if exists user;
CREATE TABLE user (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `channel_id` int(11) NOT NULL,
 `sub_channel_id` int(11) NOT NULL,
 `channel_uuid` varchar(64) NOT NULL,
 `password` varchar(64),
 `country_id` int(11) NOT NULL,
 `language_id` int(11) NOT NULL,
 `create_time` TIMESTAMP NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE INDEX unique_user_key(channel_id, sub_channel_id, channel_uuid)
);


DROP TABLE if exists user_state;
CREATE TABLE user_state (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `uuid` bigint NOT NULL,
 `token` varchar(64) NOT NULL,
 `state` int(11) NOT NULL,
 `ip` varchar(64) NOT NULL,
 `port` int(11) NOT NULL,
 `query_time` TIMESTAMP NOT NULL,
 `create_time` TIMESTAMP NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE INDEX unique_uuid(uuid),
 UNIQUE INDEX unique_token(token)
);


DROP TABLE if exists user_login_rank;
CREATE TABLE user_login_rank (
 `id` bigint NOT NULL AUTO_INCREMENT,
 `uuid` bigint NOT NULL,
 PRIMARY KEY (`id`),
 UNIQUE INDEX unique_uuid(uuid)
);