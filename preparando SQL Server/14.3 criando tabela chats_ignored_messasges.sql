drop table [chat_messages_reactions];

CREATE TABLE [chat_messages_reactions](
	[identifier] uniqueidentifier NOT NULL,
	[message_identifier] uniqueidentifier NOT NULL,
	[user_identifier] uniqueidentifier NOT NULL,
	[emoji] VARCHAR(MAX) COLLATE Latin1_General_100_CI_AI_SC_UTF8
CONSTRAINT [PK_chat_messages_reactions] PRIMARY KEY CLUSTERED 
(
	[identifier] ASC,
	[message_identifier] ASC,
	[user_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UC_chat_messages_reactions] UNIQUE NONCLUSTERED 
(
	[message_identifier] ASC,
	[user_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [chat_messages_reactions]  WITH CHECK ADD  CONSTRAINT [chat_messages_reactions_chat_messages_FK] FOREIGN KEY([message_identifier])
REFERENCES [chat_messages] ([identifier])
GO

ALTER TABLE [chat_messages_reactions]  WITH CHECK ADD  CONSTRAINT [chat_messages_reactions_users_FK] FOREIGN KEY([user_identifier])
REFERENCES [users] ([identifier])
GO