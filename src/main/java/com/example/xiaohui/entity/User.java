package com.example.xiaohui.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private String status;
}
