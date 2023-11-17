DROP TABLE IF EXISTS users_search_historic;

CREATE TABLE users_search_historic (
    identifier CHAR(36) NOT NULL,
    searcher_identifier CHAR(36),
    text VARCHAR(255),
    searched_identifier CHAR(36),
    search_date DATETIME,
    PRIMARY KEY (identifier)
);

ALTER TABLE users_search_historic
ADD CONSTRAINT users_search_historic_users_FK FOREIGN KEY (searcher_identifier)
REFERENCES users (identifier);

ALTER TABLE users_search_historic
ADD CONSTRAINT users_search_historic_users_FK2 FOREIGN KEY (searched_identifier)
REFERENCES users (identifier);