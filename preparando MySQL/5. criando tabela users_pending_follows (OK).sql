DROP TABLE IF EXISTS users_pending_follows;

CREATE TABLE users_pending_follows (
    pending_follower_identifier CHAR(36) NOT NULL,
    pending_followed_identifier CHAR(36) NOT NULL,
    PRIMARY KEY (pending_follower_identifier, pending_followed_identifier)
);

ALTER TABLE users_pending_follows
ADD CONSTRAINT users_pending_follows_users_FK
FOREIGN KEY (pending_follower_identifier)
REFERENCES users (identifier);

ALTER TABLE users_pending_follows
ADD CONSTRAINT users_pending_follows_users_FK2
FOREIGN KEY (pending_followed_identifier)
REFERENCES users (identifier);