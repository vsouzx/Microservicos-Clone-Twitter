CREATE TABLE `tweets_likes` (
	`tweet_identifier` CHAR(36) NOT NULL,
	`user_identifier` CHAR(36) NOT NULL,
	PRIMARY KEY (`tweet_identifier`, `user_identifier`)
);

ALTER TABLE `tweets_likes` 
ADD CONSTRAINT `tweets_likes_tweets_FK` FOREIGN KEY (`tweet_identifier`)
REFERENCES `tweets` (`tweet_identifier`);

ALTER TABLE `tweets_likes` 
ADD CONSTRAINT `tweets_likes_users_FK` FOREIGN KEY (`user_identifier`)
REFERENCES `users` (`identifier`);