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
	[confirmed_email] [bit] NOT NULL,
	[confirmation_code] [varchar](100),
	[registration_time] [datetime] NOT NULL,
	[private_account] [bit] NOT NULL,
	[language_preference] [varchar](10),
	[profile_photo] [VARBINARY](MAX)
	--ADD LÓGICA DE SILENCIAR (vai ser uma tabela nova)
PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

INSERT users
SELECT NEWID() -- identifier
	  ,'Vitor' -- First Name
	  ,'Souza' -- Last Name
	  ,'vtsoliveira2001@gmail.com' -- email
	  ,'vsouzx' -- username
	  ,'21y | sofredor java backend developer' --biography
	  ,'São Paulo, Brazil' --location
	  ,'https://vsportfolio.com.br' --site
	  ,'$2a$10$hMD0AWnJ.Vu0NrGZXkudDepOJxCCmRoxJQOQg6uvGuAz50J9pYZ92' --password
	  ,1 --confirmed email  (true false)
	  ,null -- confirmation code
	  ,GETDATE() -- registration_time
	  ,0 -- Private Account (true false)
	  ,'pt' --Language preference
	  ,null -- photo


