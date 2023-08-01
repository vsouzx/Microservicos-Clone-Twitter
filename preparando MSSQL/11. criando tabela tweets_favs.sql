CREATE TABLE tweets_favs(
	[tweet_identifier] [uniqueidentifier] NOT NULL,
	[user_identifier] [uniqueidentifier] NOT NULL,
	[time] DATETIME
PRIMARY KEY CLUSTERED 
(
	[tweet_identifier] ASC,
	[user_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE tweets_favs  WITH CHECK ADD  CONSTRAINT [tweets_favs_tweets_FK] FOREIGN KEY([tweet_identifier])
REFERENCES [tweets] ([tweet_identifier])
GO

ALTER TABLE tweets_favs  WITH CHECK ADD  CONSTRAINT [tweets_favs_users_FK] FOREIGN KEY([user_identifier])
REFERENCES [users] ([identifier])
GO
