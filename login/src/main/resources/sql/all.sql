DROP TABLE if exists user;
CREATE TABLE user (
    id 				INT 			PRIMARY KEY 		DEFAULT unique_rowid(),
    channel_id 		INT 			NOT NULL,
	sub_channel_id 	INT 			NOT NULL,
	channel_uuid	STRING(256) 	NOT NULL,
	password 		STRING(256),
	create_time 	TIMESTAMPTZ 						DEFAULT current_timestamp(),
	UNIQUE INDEX unique_user_key(channel_id, sub_channel_id, channel_uuid)
);


DROP TABLE if exists channel_name;
CREATE TABLE third_channel (
    id 				INT 			PRIMARY KEY 		DEFAULT unique_rowid(),
    channel_name	STRING(256)		NOT NULL,
	create_time 	TIMESTAMPTZ 						DEFAULT current_timestamp(),
	UNIQUE INDEX unique_user_key(channel_name)
);

DROP TABLE if exists country;
CREATE TABLE country (
    id 				INT 			PRIMARY KEY 		DEFAULT unique_rowid(),
    channel_name	STRING(256)		NOT NULL,
	create_time 	TIMESTAMPTZ 						DEFAULT current_timestamp(),
	UNIQUE INDEX unique_user_key(channel_name)
);