--CREATE TABLE tweets_views(
--	[tweet_identifier] [uniqueidentifier] NOT NULL,
--	[user_identifier] [uniqueidentifier] NOT NULL,
--	[time] DATETIME
--) 
--GO

--ALTER TABLE tweets_views  WITH CHECK ADD  CONSTRAINT [tweets_views_tweets_FK] FOREIGN KEY([tweet_identifier])
--REFERENCES [tweets] ([tweet_identifier])
--GO

--ALTER TABLE tweets_views  WITH CHECK ADD  CONSTRAINT [tweets_views_users_FK] FOREIGN KEY([user_identifier])
--REFERENCES [users] ([identifier])
--GO

DECLARE @idTweet uniqueidentifier = (SELECT TOP 1 tweet_identifier FROM tweets ORDER by tweet_identifier DESC),
	    @idUsuario uniqueidentifier = (SELECT TOP 1 identifier FROM users ORDER BY identifier DESC)

INSERT INTO tweets_views
SELECT @idTweet, @idUsuario, GETDATE()
