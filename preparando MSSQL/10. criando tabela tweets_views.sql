CREATE TABLE tweets_views(
	[tweet_identifier] [uniqueidentifier] NOT NULL,
	[user_identifier] [uniqueidentifier] NOT NULL
)
GO

ALTER TABLE tweets_views  WITH CHECK ADD  CONSTRAINT [tweets_views_tweets_FK] FOREIGN KEY([tweet_identifier])
REFERENCES [tweets] ([tweet_identifier])
GO

ALTER TABLE tweets_views  WITH CHECK ADD  CONSTRAINT [tweets_views_users_FK] FOREIGN KEY([user_identifier])
REFERENCES [users] ([identifier])
GO


CREATE NONCLUSTERED INDEX IX_NC_tweets_views
ON tweets_views ([tweet_identifier], [user_identifier]); 