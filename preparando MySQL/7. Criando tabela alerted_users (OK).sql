CREATE TABLE alerted_users (
    alerter_identifier CHAR(36) NOT NULL,
    alerted_identifier CHAR(36) NOT NULL,
    PRIMARY KEY (alerter_identifier, alerted_identifier)
);

ALTER TABLE alerted_users
ADD CONSTRAINT alerted_users_users_FK FOREIGN KEY (alerter_identifier)
REFERENCES users (identifier);

ALTER TABLE alerted_users
ADD CONSTRAINT alerted_users_users_FK2 FOREIGN KEY (alerted_identifier)
REFERENCES users (identifier);