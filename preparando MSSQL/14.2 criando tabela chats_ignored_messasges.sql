drop table [chat_ignored_messages];

CREATE TABLE [chat_ignored_messages](
	[chat_identifier] uniqueidentifier NOT NULL,
	[message_identifier] uniqueidentifier NOT NULL,
	[user_identifier] uniqueidentifier,
CONSTRAINT [PK_chat_ignored_messages] PRIMARY KEY CLUSTERED 
(
	[chat_identifier],
	[message_identifier],
	[user_identifier]
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [chat_ignored_messages]  WITH CHECK ADD  CONSTRAINT [chat_ignored_messages_dm_chats_FK] FOREIGN KEY([chat_identifier])
REFERENCES [dm_chats] ([identifier])
GO

ALTER TABLE [chat_ignored_messages]  WITH CHECK ADD  CONSTRAINT [chat_ignored_messages_chat_messages_FK] FOREIGN KEY([message_identifier])
REFERENCES [chat_messages] ([identifier])
GO

ALTER TABLE [chat_ignored_messages]  WITH CHECK ADD  CONSTRAINT [chat_ignored_messages_users_FK] FOREIGN KEY([user_identifier])
REFERENCES [users] ([identifier])
GO 

