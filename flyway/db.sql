CREATE DATABASE IF NOT EXISTS mybatis DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;
USE mybatis;

DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `role_id`     int(11) unsigned NOT NULL AUTO_INCREMENT,
    `role_name`   varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`role_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`        int(11) unsigned NOT NULL AUTO_INCREMENT,
    `last_name` varchar(255) DEFAULT NULL,
    `gender`    varchar(255) DEFAULT NULL,
    `email`     varchar(255) DEFAULT NULL,
    `role_id`   int(11)          NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;