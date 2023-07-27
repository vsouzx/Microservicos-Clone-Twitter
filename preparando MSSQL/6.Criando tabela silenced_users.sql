DROP TABLE silenced_users;

CREATE TABLE silenced_users(
	[silencer_identifier] [uniqueidentifier] NOT NULL,
	[silenced_identifier] [uniqueidentifier]NOT NULL,
CONSTRAINT [PK_silenced_users] PRIMARY KEY CLUSTERED 
(
	[silencer_identifier] ASC,
	[silenced_identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [silenced_users]  WITH CHECK ADD  CONSTRAINT [silenced_users_users_FK] FOREIGN KEY([silencer_identifier])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [silenced_users]  WITH CHECK ADD  CONSTRAINT [silenced_users_users_FK2] FOREIGN KEY([silenced_identifier])
REFERENCES [users] ([identifier])
GO



	   


