DROP TABLE IF EXISTS chat_messages_reactions;

CREATE TABLE chat_messages_reactions(
    identifier CHAR(36) NOT NULL,
    message_identifier CHAR(36) NOT NULL,
    user_identifier CHAR(36) NOT NULL,
    emoji TEXT,
    PRIMARY KEY (identifier, message_identifier, user_identifier),
    UNIQUE KEY UC_chat_messages_reactions (message_identifier, user_identifier)
);

ALTER TABLE chat_messages_reactions
ADD CONSTRAINT chat_messages_reactions_chat_messages_FK 
FOREIGN KEY (message_identifier) REFERENCES chat_messages (identifier);

ALTER TABLE chat_messages_reactions
ADD CONSTRAINT chat_messages_reactions_users_FK 
FOREIGN KEY (user_identifier) REFERENCES users (identifier);