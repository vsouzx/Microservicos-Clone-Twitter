CREATE TABLE tweets(
	[tweet_identifier] [uniqueidentifier] NOT NULL,
	[user_identifier] [uniqueidentifier] NOT NULL,
	[original_tweet_identifier] [uniqueidentifier] NOT NULL,
	[message] [varchar](255),
	[message_translations] [varchar](255),
	[type] [uniqueidentifier],
	[publication_time] DATETIME,
	[attachment] [VARBINARY](MAX),
	[can_be_replied_by_not_followed_user] BIT
PRIMARY KEY CLUSTERED 
(
	[tweet_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [tweets]  WITH CHECK ADD  CONSTRAINT [tweets_users_FK] FOREIGN KEY([user_identifier])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [tweets]  WITH CHECK ADD  CONSTRAINT [tweets_tweets_FK] FOREIGN KEY([original_tweet_identifier])
REFERENCES [tweets] ([tweet_identifier])
GO

ALTER TABLE [tweets]  WITH CHECK ADD  CONSTRAINT [tweets_types_FK] FOREIGN KEY([type])
REFERENCES [tweets_types] ([type_identifier])
GO

CREATE NONCLUSTERED INDEX IX_NC_tweets
ON tweets ([user_identifier]); 
