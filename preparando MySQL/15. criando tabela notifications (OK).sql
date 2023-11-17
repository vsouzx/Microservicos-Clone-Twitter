DROP TABLE IF EXISTS notifications

CREATE TABLE notifications (
    identifier CHAR(36) NOT NULL,
    tweet_identifier CHAR(36),
    user_sender_identifier CHAR(36) NOT NULL,
    user_receiver_identifier CHAR(36) NOT NULL,
    type_identifier CHAR(36) NOT NULL,
    visualized BIT NOT NULL,
    creation_date DATETIME NOT NULL,
    PRIMARY KEY (identifier)
);

ALTER TABLE notifications
ADD CONSTRAINT notifications_tweets_FK FOREIGN KEY (tweet_identifier)
REFERENCES tweets (tweet_identifier);

ALTER TABLE notifications
ADD CONSTRAINT notifications_tweets_FK2 FOREIGN KEY (type_identifier)
REFERENCES notifications_types (type_identifier);

ALTER TABLE notifications
ADD CONSTRAINT notifications_tweets_FK3 FOREIGN KEY (user_sender_identifier)
REFERENCES users (identifier);

ALTER TABLE notifications
ADD CONSTRAINT notifications_tweets_FK4 FOREIGN KEY (user_receiver_identifier)
REFERENCES users (identifier);

CREATE INDEX IX_NC_notifications ON notifications (user_receiver_identifier);