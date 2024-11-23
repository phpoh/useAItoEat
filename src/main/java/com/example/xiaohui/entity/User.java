package com.example.xiaohui.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName(value = "user")
public class User {

    @TableId
    private Long id;

    private String username;

    private String password;

    private String userip; //可以为空



}
