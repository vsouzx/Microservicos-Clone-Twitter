drop table [users_search_historic];

CREATE TABLE [users_search_historic](
	[identifier] uniqueidentifier NOT NULL,
	[searcher_identifier] uniqueidentifier,
	[text] varchar(255) COLLATE Latin1_General_100_CI_AI_SC_UTF8,
	[searched_identifier] uniqueidentifier,
	[search_date] DATETIME
CONSTRAINT [PK_users_search_historic] PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [users_search_historic]  WITH CHECK ADD  CONSTRAINT [users_search_historic_users_FK] FOREIGN KEY([searcher_identifier])
REFERENCES [users] ([identifier])
GO

ALTER TABLE [users_search_historic]  WITH CHECK ADD  CONSTRAINT [users_search_historic_users_FK2] FOREIGN KEY([searched_identifier])
REFERENCES [users] ([identifier])
GO