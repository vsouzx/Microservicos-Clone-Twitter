CREATE TABLE tweets_types(
    type_identifier CHAR(36) NOT NULL,
    description VARCHAR(20) NOT NULL,
    PRIMARY KEY (type_identifier),
    UNIQUE KEY UC_tweets_types (description)
);

INSERT INTO tweets_types (type_identifier, description) VALUES (UUID(), 'TWEET');
INSERT INTO tweets_types (type_identifier, description) VALUES (UUID(), 'RETWEET');
INSERT INTO tweets_types (type_identifier, description) VALUES (UUID(), 'COMMENT');
INSERT INTO tweets_types (type_identifier, description) VALUES (UUID(), 'NO_VALUE_RETWEET');