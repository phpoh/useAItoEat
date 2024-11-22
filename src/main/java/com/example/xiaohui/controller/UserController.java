package com.example.xiaohui.controller;

import com.example.xiaohui.dto.MessageRequest;
import com.example.xiaohui.dto.Response;
import com.example.xiaohui.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    private final String USERNAME = "admin";
    private final String PASSWORD = "123123";


    @GetMapping("/login")
    public User login(@Parameter(description = "登录用户的用户名和密码") User user) {
        if (USERNAME.equals(user.getUsername()) && PASSWORD.equals(user.getPassword())) {
            // 添加token
            user.getToken();
            return user;
        }
        return null;
    }

    @PostMapping("/chat")
    public Response handleChatRequest(@RequestBody  MessageRequest request) {
        // 获取前端传递的消息内容
        String message = request.getMessage();

        // 处理消息逻辑（比如与 AI 服务交互，这里只是简单回复）
        String responseMessage = "AI回复：" + message;

        // 延迟两秒模拟AI响应
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 返回响应
        return new Response(responseMessage);
    }
}
