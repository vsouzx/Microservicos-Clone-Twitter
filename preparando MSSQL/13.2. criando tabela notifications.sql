CREATE TABLE [notifications](
	[identifier] uniqueidentifier NOT NULL,
	[tweet_identifier] uniqueidentifier NOT NULL,
	[title] [varchar](100),
	[content] [varchar](MAX),
	[type_identifier] uniqueidentifier NOT NULL,
	[visualized] [bit] NOT NULL,
	[creation_date] [datetime] NOT NULL
CONSTRAINT [PK_notifications] PRIMARY KEY CLUSTERED 
(
	[identifier] ASC,
	[tweet_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [notifications]  WITH CHECK ADD  CONSTRAINT [notifications_tweets_FK] FOREIGN KEY([tweet_identifier])
REFERENCES [tweets] ([tweet_identifier])
GO

ALTER TABLE [notifications]  WITH CHECK ADD  CONSTRAINT [notifications_tweets_FK2] FOREIGN KEY([type_identifier])
REFERENCES [notifications_types] ([type_identifier])
GO