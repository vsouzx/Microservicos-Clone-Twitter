drop table [dm_chats]

CREATE TABLE [dm_chats](
	[identifier] uniqueidentifier NOT NULL,
	[user_identifier_1] uniqueidentifier NOT NULL,
	[user_identifier_2] uniqueidentifier NOT NULL,
CONSTRAINT [PK_dm_chats] PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dm_chats]  WITH CHECK ADD  CONSTRAINT [dm_chats_users_FK] FOREIGN KEY([user_identifier_1])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [dm_chats]  WITH CHECK ADD  CONSTRAINT [dm_chats_users_FK2] FOREIGN KEY([user_identifier_2])
REFERENCES [users] ([identifier])
GO

CREATE NONCLUSTERED INDEX IX_NC_dm_chats
ON [dm_chats] ([user_identifier_1], [user_identifier_2]); 