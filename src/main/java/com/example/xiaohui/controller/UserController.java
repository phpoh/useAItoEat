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

@Tag(name = "用户管理", description = "用户管理API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    private final String USERNAME = "admin";
    private final String PASSWORD = "123123";

    @Operation(summary = "用户登录",
            description = "用户通过提供用户名和密码进行登录，成功则返回用户信息和 token。",
            responses = {
                    @ApiResponse(responseCode = "200", description = "登录成功，返回用户信息和 token"),
                    @ApiResponse(responseCode = "401", description = "用户名或密码错误")
            })
    @GetMapping("/login")
    public User login(@Parameter(description = "登录用户的用户名和密码") User user) {
        if (USERNAME.equals(user.getUsername()) && PASSWORD.equals(user.getPassword())) {
            // 添加token
            user.getToken();
            return user;
        }
        return null;
    }

    @Operation(summary = "用户与AI对话",
            description = "用户发送消息后，AI系统处理并返回回复消息。",
            responses = {
                    @ApiResponse(responseCode = "200", description = "AI成功回复消息"),
                    @ApiResponse(responseCode = "500", description = "服务器错误")
            })
    @PostMapping("/chat")
    public Response handleChatRequest(
            @RequestBody @Parameter(description = "用户发送的消息") MessageRequest request) {
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
