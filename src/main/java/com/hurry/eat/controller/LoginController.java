package com.hurry.eat.controller;

import com.hurry.eat.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/login")
public class LoginController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password) {
        if (authenticationService.authenticate(username, password)) {
            // 返回登录成功的响应，通常是 JWT 或会话信息
            return "Login successful";
        }
        return "Invalid username or password";
    }
}
