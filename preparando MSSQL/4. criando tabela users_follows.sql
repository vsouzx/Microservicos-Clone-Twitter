
CREATE TABLE users_follows(
	[follower_identifier] [uniqueidentifier] NOT NULL,
	[followed_identifier] [uniqueidentifier]NOT NULL,
CONSTRAINT [PK_users_follows] PRIMARY KEY CLUSTERED 
(
	[follower_identifier] ASC,
	[followed_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [users_follows]  WITH CHECK ADD  CONSTRAINT [users_follows_users_FK] FOREIGN KEY([follower_identifier])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [users_follows]  WITH CHECK ADD  CONSTRAINT [users_follows_users_FK2] FOREIGN KEY([followed_identifier])
REFERENCES [users] ([identifier])
GO




