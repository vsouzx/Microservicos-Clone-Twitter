DROP TABLE blocked_users;

CREATE TABLE blocked_users(
	[blocker_identifier] [uniqueidentifier] NOT NULL,
	[blocked_identifier] [uniqueidentifier]NOT NULL,
CONSTRAINT [PK_blocked_users] PRIMARY KEY CLUSTERED 
(
	[blocker_identifier] ASC,
	[blocked_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [blocked_users]  WITH CHECK ADD  CONSTRAINT [blocked_users_users_FK] FOREIGN KEY([blocker_identifier])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [blocked_users]  WITH CHECK ADD  CONSTRAINT [blocked_users_users_FK2] FOREIGN KEY([blocked_identifier])
REFERENCES [users] ([identifier])
GO



	   


