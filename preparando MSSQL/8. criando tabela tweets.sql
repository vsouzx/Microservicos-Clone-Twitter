--CREATE TABLE tweets(
--	[tweet_identifier] [uniqueidentifier] NOT NULL,
--	[user_identifier] [uniqueidentifier] NOT NULL,
--	[original_tweet_identifier] [uniqueidentifier] NOT NULL,
--	[message] [varchar](255),
--	[message_translations] [varchar](255),
--	[type] [uniqueidentifier],
--	[publication_time] DATETIME,
--	[attachment] [VARBINARY](MAX),
--	[can_be_replied_by_not_followed_user] BIT
--PRIMARY KEY CLUSTERED 
--(
--	[tweet_identifier] ASC
--)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
--) ON [PRIMARY]
--GO

--ALTER TABLE [tweets]  WITH CHECK ADD  CONSTRAINT [tweets_users_FK] FOREIGN KEY([user_identifier])
--REFERENCES [users] ([identifier])
--GO

--ALTER TABLE [tweets]  WITH CHECK ADD  CONSTRAINT [tweets_tweets_FK] FOREIGN KEY([original_tweet_identifier])
--REFERENCES [tweets] ([tweet_identifier])
--GO

--ALTER TABLE [tweets]  WITH CHECK ADD  CONSTRAINT [tweets_types_FK] FOREIGN KEY([type])
--REFERENCES [tweets_types] ([type_identifier])
--GO

DECLARE @idTweet uniqueidentifier = (SELECT NEWID()),
		@idType uniqueidentifier = (SELECT type_identifier FROM tweets_types WHERE description = 'COMMENT')

INSERT INTO tweets
SELECT @idTweet -- Id tweet
	  ,'39077E3A-805D-46E8-A742-B492C7CDFE79' -- Id user
	  ,@idTweet -- Id do tweet original (se NULL, é o proprio tweet mesmo)
	  ,'gosto, mas odeio node js' -- mensagem do tweet
	  ,'i do, but hate node js' -- traducao da mensagem
	  ,@idType --tipo do tweet
	  ,GETDATE() -- hora que o tweet foi publicado
	  ,null --anexo



