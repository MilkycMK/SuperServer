-- REGISTRATION
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_name VARCHAR(32) NOT NULL,
    password VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS sessions (
    id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id INTEGER NOT NULL,
    token VARCHAR(255) NOT NULL,
    mask VARCHAR(4) NOT NULL,
    creation_time DATETIME NOT NULL,
    expiration_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS `groups` (
    id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    user_id INTEGER NOT NULL,
    group_name VARCHAR(32) NOT NULL
);