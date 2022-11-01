CREATE TABLE if not exists comment
(
    comment_id      bigint PRIMARY KEY AUTO_INCREMENT,
    user_id   bigint,
    post_id        bigint,
    message      varchar(255),
    soft_removed bit(1),
    parent_id    bigint,
    create_at    timestamp,
    update_at    timestamp
) engine = InnoDB;

ALTER TABLE comment
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE comment
    ADD FOREIGN KEY (post_id) REFERENCES post (post_id);