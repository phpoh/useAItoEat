package com.hurry.eat.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
// 表示这个类是一个 JPA 实体类，它会映射到数据库中的表
@Entity
public class User {
    // 指定主键字段，JPA 会根据它来生成数据库表的主键。
    @Id
    private Long id;

    // 表示数据库表中的列。unique = true 表示 username 字段是唯一的，nullable = false 表示该字段不能为空
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}
