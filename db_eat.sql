USE db_eat;

CREATE TABLE user (
                      id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                      username VARCHAR(255) NOT NULL COMMENT '用户名',
                      password VARCHAR(255) NOT NULL COMMENT '密码',
                      userip VARCHAR(255) DEFAULT NULL COMMENT '用户IP，可以为空',
                      PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

