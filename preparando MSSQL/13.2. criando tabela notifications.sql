CREATE TABLE [notifications](
	[identifier] uniqueidentifier NOT NULL,
	[tweet_identifier] uniqueidentifier,
	[user_sender_identifier] uniqueidentifier NOT NULL,
	[user_receiver_identifier] uniqueidentifier NOT NULL,
	[type_identifier] uniqueidentifier NOT NULL,
	[visualized] [bit] NOT NULL,
	[creation_date] [datetime] NOT NULL
CONSTRAINT [PK_notifications] PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [notifications]  WITH CHECK ADD  CONSTRAINT [notifications_tweets_FK] FOREIGN KEY([tweet_identifier])
REFERENCES [tweets] ([tweet_identifier])
GO

ALTER TABLE [notifications]  WITH CHECK ADD  CONSTRAINT [notifications_tweets_FK2] FOREIGN KEY([type_identifier])
REFERENCES [notifications_types] ([type_identifier])
GO

ALTER TABLE [notifications]  WITH CHECK ADD  CONSTRAINT [notifications_tweets_FK3] FOREIGN KEY([user_sender_identifier])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [notifications]  WITH CHECK ADD  CONSTRAINT [notifications_tweets_FK4] FOREIGN KEY([user_receiver_identifier])
REFERENCES [users] ([identifier])
GO

CREATE NONCLUSTERED INDEX IX_NC_notifications
ON notifications ([user_receiver_identifier]); 