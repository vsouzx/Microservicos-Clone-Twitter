DROP TABLE IF EXISTS blocked_users;

CREATE TABLE blocked_users (
    blocker_identifier CHAR(36) NOT NULL,  -- uniqueidentifier é similar ao CHAR(36) em MySQL
    blocked_identifier CHAR(36) NOT NULL,
    PRIMARY KEY (blocker_identifier, blocked_identifier)
);

ALTER TABLE blocked_users
ADD CONSTRAINT blocked_users_users_FK FOREIGN KEY (blocker_identifier)
REFERENCES users (identifier);

ALTER TABLE blocked_users
ADD CONSTRAINT blocked_users_users_FK2 FOREIGN KEY (blocked_identifier)
REFERENCES users (identifier);
