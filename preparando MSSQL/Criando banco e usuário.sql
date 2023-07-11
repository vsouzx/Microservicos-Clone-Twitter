CREATE DATABASE twitterclone_dev;

IF EXISTS (SELECT name FROM sys.sql_logins where name = 'twitterclone_admin')
	DROP LOGIN twitterclone_admin
GO

IF EXISTS (SELECT name FROM sys.sysusers where name = 'twitterclone_admin')
	DROP USER twitterclone_admin
GO

--Criando LOGIN para schema ABSWEB_CHATBOT
CREATE LOGIN twitterclone_admin WITH PASSWORD ='admin123', DEFAULT_DATABASE=[twitterclone_dev], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF
GO

--Criando USER para schema ABSWEB_CHATBOT
CREATE USER twitterclone_admin FOR LOGIN twitterclone_admin WITH DEFAULT_SCHEMA = dbo;

--dando permissoes
GRANT CONTROL 
ON SCHEMA::dbo
TO twitterclone_admin;