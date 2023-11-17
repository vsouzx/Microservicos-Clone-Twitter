DROP TABLE IF EXISTS users;

CREATE TABLE users (
	identifier CHAR(36) NOT NULL,
	first_name VARCHAR(255) NOT NULL,
	birth_date DATETIME NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	username VARCHAR(30) NOT NULL UNIQUE,
	biography VARCHAR(255),
	location VARCHAR(100),
	site VARCHAR(255),
	password VARCHAR(255) NOT NULL,
	confirmed_email TINYINT(1) NOT NULL,
	confirmation_code VARCHAR(100),
	registration_time DATETIME NOT NULL,
	private_account TINYINT(1) NOT NULL,
	language_preference VARCHAR(10),
	profile_photo_url TEXT,
	background_photo_url TEXT,
	first_access TINYINT(1) NOT NULL,
	verified TINYINT(1) NOT NULL,
	PRIMARY KEY (identifier)
);