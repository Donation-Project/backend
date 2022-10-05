CREATE TABLE `users` (
                         `id` long PRIMARY KEY AUTO_INCREMENT,
                         `email` varchar(255) UNIQUE NOT NULL,
                         `password` varchar(255),
                         `name` varchar(255),
                         `role` ENUM ('USER', 'ADMIN'),
                         `create_at` timestamp,
                         `update_at` timestamp
);

CREATE TABLE `inquiry` (
                           `id` long PRIMARY KEY AUTO_INCREMENT,
                           `user_id` long,
                           `title` text,
                           `content` text,
                           `status` ENUM ('RESISTRATION', 'DONATION', 'ETC'),
                           `created_at` timestamp,
                           `updated_at` timestamp
);

CREATE TABLE `answer` (
                          `id` long PRIMARY KEY AUTO_INCREMENT,
                          `inquiry_id` long,
                          `title` text,
                          `content` text,
                          `created_at` timestamp,
                          `updated_at` timestamp
);

CREATE TABLE `favorites` (
                             `id` long PRIMARY KEY AUTO_INCREMENT,
                             `user_id` long,
                             `post_id` long,
                             `create_at` timestamp,
                             `update_at` timestamp
);

CREATE TABLE `post` (
                        `id` long PRIMARY KEY AUTO_INCREMENT,
                        `user_id` long,
                        `category` ENUM ('ETC'),
                        `title` text,
                        `content` text,
                        `amount` integer,
                        `status` ENUM ('WAITING', 'APPROVAL', 'COMPLETION'),
                        `create_at` timestamp,
                        `update_at` timestamp
);

CREATE TABLE `post_wait` (
                             `id` long PRIMARY KEY AUTO_INCREMENT,
                             `user_id` long,
                             `category` ENUM ('ETC'),
                             `title` text,
                             `content` text,
                             `amount` integer,
                             `create_at` timestamp,
                             `update_at` timestamp
);

CREATE TABLE `post_detail_images` (
                                      `id` long PRIMARY KEY AUTO_INCREMENT,
                                      `post_id` long,
                                      `imagepath` varchar(255),
                                      `created_at` timestamp,
                                      `updated_at` timestamp
);

CREATE TABLE `donation` (
                            `id` long PRIMARY KEY AUTO_INCREMENT,
                            `users_id` long,
                            `post_id` long,
                            `create_at` timestamp,
                            `update_at` timestamp
);

CREATE TABLE `reviews` (
                           `id` long PRIMARY KEY AUTO_INCREMENT,
                           `users_id` long,
                           `post_id` long,
                           `title` text,
                           `content` text,
                           `created_at` timestamp,
                           `updated_at` timestamp
);

ALTER TABLE `inquiry` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `answer` ADD FOREIGN KEY (`inquiry_id`) REFERENCES `inquiry` (`id`);

ALTER TABLE `favorites` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `favorites` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `post` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `post_wait` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `post_detail_images` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `donation` ADD FOREIGN KEY (`users_id`) REFERENCES `users` (`id`);

ALTER TABLE `donation` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);

ALTER TABLE `reviews` ADD FOREIGN KEY (`users_id`) REFERENCES `users` (`id`);

ALTER TABLE `reviews` ADD FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);
