package com.example.xiaohui.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class User {

    @Schema(description = "用户名，用于用户登录", example = "admin")
    private String username;

    @Schema(description = "密码，用于用户登录", example = "123123")
    private String password;

    @Schema(description = "用户的认证 token", example = "abcd1234xyz")
    private String token;
}
