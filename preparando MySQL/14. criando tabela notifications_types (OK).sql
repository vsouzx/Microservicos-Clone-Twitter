CREATE TABLE notifications_types (
    type_identifier CHAR(36) NOT NULL,
    description VARCHAR(50) NOT NULL,
    PRIMARY KEY (type_identifier),
    UNIQUE KEY UC_notifications_types (description)
);

INSERT INTO notifications_types (type_identifier, description) VALUES
(UUID(), 'NEW_FOLLOWER'),
(UUID(), 'NEW_COMMENT'),
(UUID(), 'NEW_LIKE'),
(UUID(), 'NEW_POST'),
(UUID(), 'NEW_RETWEET');