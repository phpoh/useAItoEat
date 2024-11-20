package com.example.xiaohui.controller;

import com.example.xiaohui.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final String USERNAME = "admin";
    private final String PASSWORD = "123123";

    @GetMapping("/login")
    public User login(User user){
        if (USERNAME.equals(user.getUsername()) && PASSWORD.equals(user.getPassword())){
            //添加token
            user.getToken();
            return user;
        }
        return null;
    }
}
