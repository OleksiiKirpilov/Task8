CREATE TABLE `users`
(
    `id`    INT         NOT NULL AUTO_INCREMENT,
    `login` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `login` (`login`)
);

CREATE TABLE `teams`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
);

CREATE TABLE `users_teams`
(
    `user_id` INT REFERENCES `users` (`id`) ON DELETE CASCADE,
    `team_id` INT REFERENCES `teams` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE,
    UNIQUE (`user_id`, `team_id`)
);

INSERT INTO `users`
VALUES (1, 'ivanov');
INSERT INTO `teams`
VALUES (1, 'teamA');
INSERT INTO `users_teams`
VALUES (1, 1);