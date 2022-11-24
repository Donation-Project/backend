CREATE TABLE if not exists authcode
(
    auth_id              bigint PRIMARY KEY AUTO_INCREMENT,
    code                 varchar(255) NOT NULL,
    auth_serial_number   varchar(255) NOT NULL,
    create_at            timestamp,
    update_at            timestamp
) engine = InnoDB;