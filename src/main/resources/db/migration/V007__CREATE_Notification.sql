CREATE TABLE if not exists notification
(
    notification_id              bigint PRIMARY KEY AUTO_INCREMENT,
    user_id            bigint NOT NULL,
    post_id              bigint,
    comment_id           bigint,
    type                 varchar(255),
    conform            bit(1),
    create_at            timestamp,
    update_at            timestamp
) engine = InnoDB;

ALTER TABLE notification
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE notification
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id);

ALTER TABLE notification
    ADD FOREIGN KEY (comment_id) REFERENCES comment (comment_id);