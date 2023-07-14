DROP TABLE users;

CREATE TABLE users(
	[identifier] [uniqueidentifier] NOT NULL,
	[first_name] [varchar](255) NOT NULL,
	[last_name] [varchar](255) NOT NULL,
	[email] [varchar](255) NOT NULL UNIQUE,
	[username] [varchar](30) NOT NULL UNIQUE,
	[biography] [varchar](255),
	[location][varchar](100),
	[site][varchar](255),
	[password] [varchar](255) NOT NULL,
	[confirmed_email] [bit],
	[confirmation_code] [varchar](100),
	[registration_time] [datetime]
PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

INSERT users
SELECT NEWID(), 'Vitor', 'Souza', 'vtsoliveira2001@gmail.com', 'vsouzx', '21y | sofredor java backend developer', 'São Paulo, Brazil', 'https://vsportfolio.com.br', '$2a$10$hMD0AWnJ.Vu0NrGZXkudDepOJxCCmRoxJQOQg6uvGuAz50J9pYZ92', 1, null, GETDATE()

