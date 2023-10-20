drop table [chat_messages];

CREATE TABLE [chat_messages](
	[identifier] uniqueidentifier NOT NULL,
	[chat_identifier] uniqueidentifier NOT NULL,
	[user_identifier] uniqueidentifier NOT NULL,
	[text] VARCHAR(MAX) COLLATE Latin1_General_100_CI_AI_SC_UTF8,
	[tweet_identifier] uniqueidentifier,
	[creation_date] [datetime] NOT NULL,
	[seen] BIT
CONSTRAINT [PK_chat_messages] PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [chat_messages]  WITH CHECK ADD  CONSTRAINT [chat_messages_dm_chats_FK] FOREIGN KEY([chat_identifier])
REFERENCES [dm_chats] ([identifier])
GO

ALTER TABLE [chat_messages]  WITH CHECK ADD  CONSTRAINT [chat_messages_users_FK] FOREIGN KEY([user_identifier])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [chat_messages]  WITH CHECK ADD  CONSTRAINT [chat_messages_tweets_FK] FOREIGN KEY([tweet_identifier])
REFERENCES [tweets] ([tweet_identifier])
GO

CREATE NONCLUSTERED INDEX IX_NC_chat_messages
ON [chat_messages] ([user_identifier]); 