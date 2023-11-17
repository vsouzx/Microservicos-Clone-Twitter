DROP TABLE IF EXISTS dm_chats;

CREATE TABLE dm_chats (
    identifier CHAR(36) NOT NULL,
    user_identifier_1 CHAR(36) NOT NULL,
    user_identifier_2 CHAR(36) NOT NULL,
    PRIMARY KEY (identifier)
);

ALTER TABLE dm_chats
ADD CONSTRAINT dm_chats_users_FK FOREIGN KEY (user_identifier_1)
REFERENCES users (identifier);

ALTER TABLE dm_chats
ADD CONSTRAINT dm_chats_users_FK2 FOREIGN KEY (user_identifier_2)
REFERENCES users (identifier);

CREATE INDEX IX_NC_dm_chats ON dm_chats (user_identifier_1, user_identifier_2);