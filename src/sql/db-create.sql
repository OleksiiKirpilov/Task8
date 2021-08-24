DROP DATABASE IF EXISTS p8db;
CREATE DATABASE p8db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE p8db;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`    INT         NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `login` (`login`)
);

INSERT INTO `users`
VALUES (1, 'ivanov');

DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
);

INSERT INTO `teams`
VALUES (1, 'teamA');


DROP TABLE IF EXISTS `users_teams`;
CREATE TABLE `users_teams`
(
    `user_id` INT REFERENCES users (id) ON DELETE CASCADE,
    `team_id` INT REFERENCES teams (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    UNIQUE (`user_id`, `team_id`)
);

INSERT INTO `users_teams`
VALUES (1, 1);

