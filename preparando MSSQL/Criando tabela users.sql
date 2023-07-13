CREATE TABLE dbo.users(
	[identifier] [uniqueidentifier] NOT NULL,
	[firstName] [varchar](255) NOT NULL,
	[lastName] [varchar](255) NOT NULL,
	[email] [varchar](255) NOT NULL UNIQUE,
	[username] [varchar](30) NOT NULL UNIQUE,
	[password] [varchar](255) NOT NULL,
	[confirmed_email] [bit]
PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

INSERT users
SELECT NEWID(), 'Vitor', 'Souza', 'vtsoliveira2001@gmail.com', 'vsouzx', '$2a$10$hMD0AWnJ.Vu0NrGZXkudDepOJxCCmRoxJQOQg6uvGuAz50J9pYZ92', 0

