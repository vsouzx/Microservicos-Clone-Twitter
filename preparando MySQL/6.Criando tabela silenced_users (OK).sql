CREATE TABLE silenced_users (
  silencer_identifier CHAR(36) NOT NULL,
  silenced_identifier CHAR(36) NOT NULL,
  PRIMARY KEY (silencer_identifier, silenced_identifier)
);

ALTER TABLE silenced_users 
ADD CONSTRAINT silenced_users_users_FK 
FOREIGN KEY (silencer_identifier) 
REFERENCES users (identifier);

ALTER TABLE silenced_users 
ADD CONSTRAINT silenced_users_users_FK2 
FOREIGN KEY (silenced_identifier) 
REFERENCES users (identifier);