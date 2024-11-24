package com.example.xiaohui.controller;

import com.example.xiaohui.dto.MessageRequest;
import com.example.xiaohui.dto.Response;
import com.example.xiaohui.entity.User;
import com.example.xiaohui.util.JwtUtil;
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



    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // 假设用户名和密码校验成功（替换为你的实际校验逻辑）
        if ("admin".equals(username) && "123456".equals(password)) {
            // 生成 JWT token
            return JwtUtil.generateToken(username);
        }
        return "Invalid username or password";
    }

    @PostMapping("/chat")
    public Response handleChatRequest(@RequestBody MessageRequest request) {
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


    // 用于前端确认token有效而放行index.html主页
    @GetMapping("/verify")
    public String sayHello(){
        return "pass";
    }





}
