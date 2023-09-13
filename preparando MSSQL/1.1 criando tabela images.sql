CREATE TABLE images(
	[identifier] [uniqueidentifier] NOT NULL,
	[photo] [varbinary](MAX) ,
	[Xposition] INT ,
	[Yposition] INT ,
	PRIMARY KEY CLUSTERED 
(
	[identifier] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY])
ON [PRIMARY]
GO