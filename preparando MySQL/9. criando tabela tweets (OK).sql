CREATE TABLE tweets (
    tweet_identifier CHAR(36) NOT NULL,
    user_identifier CHAR(36) NOT NULL,
    original_tweet_identifier CHAR(36) NOT NULL,
    message VARCHAR(255),
    message_translations VARCHAR(255),
    type CHAR(36),
    publication_time DATETIME,
    can_be_replied_by_not_followed_user BIT,
    PRIMARY KEY (tweet_identifier)
);

ALTER TABLE tweets
ADD CONSTRAINT tweets_users_FK FOREIGN KEY (user_identifier)
REFERENCES users (identifier);

ALTER TABLE tweets
ADD CONSTRAINT tweets_tweets_FK FOREIGN KEY (original_tweet_identifier)
REFERENCES tweets (tweet_identifier);

ALTER TABLE tweets
ADD CONSTRAINT tweets_types_FK FOREIGN KEY (type)
REFERENCES tweets_types (type_identifier);

CREATE INDEX IX_NC_tweets ON tweets (user_identifier);