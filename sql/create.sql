CREATE TABLE if not exists users (
      user_id bigint PRIMARY KEY AUTO_INCREMENT,
      username varchar(255) UNIQUE NOT NULL,
      password varchar(255),
      name varchar(255),
      profileImage varchar(255),
      role varchar(255),
      provide varchar(255),
      providerId  varchar(255),
      metamask  varchar(255),
      create_at  timestamp,
      update_at  timestamp
)engine = InnoDB;

CREATE TABLE if not exists post  (
    post_id  bigint  PRIMARY KEY AUTO_INCREMENT,
    user_id  bigint,
    status  varchar(255),
    category  varchar(255),
    title  varchar(255),
    content  longtext,
    amount  float,
    create_at  timestamp,
    update_at  timestamp
)engine = InnoDB;

CREATE TABLE if not exists post_detail_images  (
    post_detail_id  bigint PRIMARY KEY AUTO_INCREMENT,
    post_id  bigint,
    imagepath  varchar(255),
    created_at  timestamp,
    updated_at  timestamp
)engine = InnoDB;

CREATE TABLE if not exists inquiry   (
    inquiry_id  bigint PRIMARY KEY AUTO_INCREMENT,
    user_id  bigint,
    title  varchar(255),
    content  longtext,
    status  varchar(255),
    created_at  timestamp,
    updated_at  timestamp
)engine = InnoDB;

CREATE TABLE if not exists answer  (
    answer_id  bigint PRIMARY KEY AUTO_INCREMENT,
    inquiry_id  bigint,
    title  varchar(255),
    content  longtext,
    created_at  timestamp,
    updated_at  timestamp
)engine = InnoDB;

CREATE TABLE if not exists favorites  (
    favorites_id  bigint PRIMARY KEY AUTO_INCREMENT,
    user_id  bigint,
    post_id  bigint,
    create_at  timestamp,
    update_at  timestamp
)engine = InnoDB;

CREATE TABLE if not exists donation  (
    donation_id  bigint PRIMARY KEY AUTO_INCREMENT,
    users_id  bigint,
    post_id  bigint,
    amount  float,
    create_at  timestamp,
    update_at  timestamp
)engine = InnoDB;

CREATE TABLE if not exists reviews  (
    reviews_id  bigint PRIMARY KEY AUTO_INCREMENT,
    users_id  bigint,
    post_id  bigint,
    title  varchar(255),
    content  longtext,
    created_at  timestamp,
    updated_at  timestamp
)engine = InnoDB;

ALTER TABLE  post  ADD FOREIGN KEY ( user_id ) REFERENCES  users  ( user_id );

ALTER TABLE  post_detail_images  ADD FOREIGN KEY ( post_id ) REFERENCES  post  ( post_id );

ALTER TABLE  inquiry  ADD FOREIGN KEY ( user_id ) REFERENCES  users  ( user_id );

ALTER TABLE  answer  ADD FOREIGN KEY ( inquiry_id ) REFERENCES  inquiry  ( inquiry_id );

ALTER TABLE  favorites  ADD FOREIGN KEY ( user_id ) REFERENCES  users  ( user_id );

ALTER TABLE  favorites  ADD FOREIGN KEY ( post_id ) REFERENCES  post  ( post_id );

ALTER TABLE  donation  ADD FOREIGN KEY ( users_id ) REFERENCES  users  ( user_id );

ALTER TABLE  donation  ADD FOREIGN KEY ( post_id ) REFERENCES  post  ( post_id );

ALTER TABLE  reviews  ADD FOREIGN KEY ( users_id ) REFERENCES  users  ( user_id );

ALTER TABLE  reviews  ADD FOREIGN KEY ( post_id ) REFERENCES  post  ( post_id );
