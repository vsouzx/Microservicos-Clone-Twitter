DROP TABLE users;

CREATE TABLE users(
	[identifier] [uniqueidentifier] NOT NULL,
	[first_name] [varchar](255) NOT NULL,
	[birth_date] [DATETIME] NULL,
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
	[profile_photo] [VARBINARY](MAX),
	[background_photo] [VARBINARY](MAX),
	[first_access] [BIT] NOT NULL
PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

INSERT users
SELECT NEWID() -- identifier
	  ,'Vinicius' -- First Name
	  ,'Oliveira' -- Last Name
	  ,'vnsoliveira2512@gmail.com' -- email
	  ,'vincius123' -- username
	  ,'FRONTEND DEV' --biography
	  ,'São Paulo, Brazil' --location
	  ,'https://youtube.com.br' --site
	  ,'$2a$10$hMD0AWnJ.Vu0NrGZXkudDepOJxCCmRoxJQOQg6uvGuAz50J9pYZ92' --password
	  ,1 --confirmed email  (true false)
	  ,null -- confirmation code
	  ,GETDATE() -- registration_time
	  ,0 -- Private Account (true false)
	  ,'pt' --Language preference
	  ,null -- photo


