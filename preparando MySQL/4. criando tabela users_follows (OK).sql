CREATE TABLE users_follows (
    follower_identifier CHAR(36) NOT NULL,
    followed_identifier CHAR(36) NOT NULL,
    PRIMARY KEY (follower_identifier, followed_identifier)
);

ALTER TABLE users_follows 
ADD CONSTRAINT users_follows_users_FK 
FOREIGN KEY (follower_identifier) 
REFERENCES users (identifier);

ALTER TABLE users_follows 
ADD CONSTRAINT users_follows_users_FK2 
FOREIGN KEY (followed_identifier) 
REFERENCES users (identifier);