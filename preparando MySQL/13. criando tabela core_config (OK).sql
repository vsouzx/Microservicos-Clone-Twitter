CREATE TABLE core_config(
    identifier CHAR(36) NOT NULL, 
    key_name VARCHAR(20) NOT NULL,
    key_value LONGTEXT NOT NULL, 
    PRIMARY KEY (identifier),
    UNIQUE KEY UC_core_config (key_name)
);