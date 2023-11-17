DROP TABLE IF EXISTS chat_ignored_messages;

CREATE TABLE chat_ignored_messages (
    chat_identifier CHAR(36) NOT NULL,
    message_identifier CHAR(36) NOT NULL,
    user_identifier CHAR(36),
    PRIMARY KEY (chat_identifier, message_identifier, user_identifier)
);

ALTER TABLE chat_ignored_messages
    ADD CONSTRAINT chat_ignored_messages_dm_chats_FK
    FOREIGN KEY (chat_identifier) REFERENCES dm_chats (identifier);

ALTER TABLE chat_ignored_messages
    ADD CONSTRAINT chat_ignored_messages_chat_messages_FK
    FOREIGN KEY (message_identifier) REFERENCES chat_messages (identifier);

ALTER TABLE chat_ignored_messages
    ADD CONSTRAINT chat_ignored_messages_users_FK
    FOREIGN KEY (user_identifier) REFERENCES users (identifier);