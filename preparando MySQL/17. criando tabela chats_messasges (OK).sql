DROP TABLE IF EXISTS chat_messages;

CREATE TABLE chat_messages (
	identifier CHAR(36) NOT NULL,
	chat_identifier CHAR(36) NOT NULL,
	user_identifier CHAR(36) NOT NULL,
	text TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
	tweet_identifier CHAR(36),
	creation_date DATETIME NOT NULL,
	seen BIT,
	PRIMARY KEY (identifier)
);

-- Add foreign key constraints
ALTER TABLE chat_messages
ADD CONSTRAINT chat_messages_dm_chats_FK
FOREIGN KEY (chat_identifier) REFERENCES dm_chats (identifier);

ALTER TABLE chat_messages
ADD CONSTRAINT chat_messages_users_FK
FOREIGN KEY (user_identifier) REFERENCES users (identifier);

ALTER TABLE chat_messages
ADD CONSTRAINT chat_messages_tweets_FK
FOREIGN KEY (tweet_identifier) REFERENCES tweets (tweet_identifier);

-- Create an index
CREATE INDEX IX_NC_chat_messages ON chat_messages (user_identifier);