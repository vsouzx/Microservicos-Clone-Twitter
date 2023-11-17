CREATE TABLE tweets_favs (
    tweet_identifier CHAR(36) NOT NULL,
    user_identifier CHAR(36) NOT NULL,
    time DATETIME,
    PRIMARY KEY (tweet_identifier, user_identifier)
);

ALTER TABLE tweets_favs 
ADD CONSTRAINT tweets_favs_tweets_FK FOREIGN KEY (tweet_identifier)
REFERENCES tweets (tweet_identifier);

ALTER TABLE tweets_favs 
ADD CONSTRAINT tweets_favs_users_FK FOREIGN KEY (user_identifier)
REFERENCES users (identifier);