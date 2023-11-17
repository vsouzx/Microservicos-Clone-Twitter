CREATE TABLE alerted_users(
	[alerter_identifier] [uniqueidentifier] NOT NULL,
	[alerted_identifier] [uniqueidentifier]NOT NULL,
CONSTRAINT [PK_alerted_users] PRIMARY KEY CLUSTERED 
(
	[alerter_identifier] ASC,
	[alerted_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [alerted_users]  WITH CHECK ADD  CONSTRAINT [alerted_users_users_FK] FOREIGN KEY([alerter_identifier])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [alerted_users]  WITH CHECK ADD  CONSTRAINT [alerted_users_users_FK2] FOREIGN KEY([alerted_identifier])
REFERENCES [users] ([identifier])
GO



	   


