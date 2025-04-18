package com.example.xiaohui.controller;

import com.example.xiaohui.dto.MessageRequest;
import com.example.xiaohui.dto.Response;
import com.example.xiaohui.entity.User;
import com.example.xiaohui.service.UserService;
import com.example.xiaohui.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // 从服务中获取用户名对应的用户（假设 UserService 实现了获取用户的逻辑）
        User user = userService.getUsername(username);

        // 如果用户存在
        if (user != null) {
            // 比较数据库中存储的密码和输入的密码
            if (user.getPassword().equals(password)) {
                // 密码匹配，生成 JWT token（假设 JwtUtil 是一个工具类）
                return JwtUtil.generateToken(user.getUsername());
            } else {
                // 密码不正确
                return "Invalid password";
            }
        } else {
            // 用户不存在
            return "User not found";
        }
    }


    @PostMapping("/chat")
    public Response handleChatRequest(@RequestBody MessageRequest request,@RequestHeader("Authorization") String authorization) {
        // 获取前端传递的消息内容
        String message = request.getMessage();

        // 从 Authorization 头部获取 token
        String token = authorization;

        if (token == null || !JwtUtil.isValidToken(token)) {
            // 如果 token 无效，返回 401 未授权
            return new Response("未授权访问，Token 无效");
        }

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
    @PostMapping("/verify")
    public Response verify(@RequestHeader("Authorization") String authorization) {
        // 从 Authorization 头部获取 token
        String token = authorization;
        if (token == null || !JwtUtil.isValidToken(token)) {
            // 如果 token 无效，返回 401 未授权
            return new Response("未授权访问Token无效");
        }
        // 返回响应
        return new Response("Tokenok");
    }



    //获取所有用户信息（三个用户）
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


}
