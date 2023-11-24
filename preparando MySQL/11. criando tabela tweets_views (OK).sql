CREATE TABLE tweets_views (
    tweet_identifier CHAR(36) NOT NULL,
    user_identifier CHAR(36) NOT NULL,
    time DATETIME,
    FOREIGN KEY (tweet_identifier) REFERENCES tweets (tweet_identifier),
    FOREIGN KEY (user_identifier) REFERENCES users (identifier)
);

CREATE INDEX IX_NC_tweets_views ON tweets_views (tweet_identifier, user_identifier);