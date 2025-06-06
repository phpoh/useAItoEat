package com.example.xiaohui.controller;

import com.example.xiaohui.dto.MessageRequest;
import com.example.xiaohui.dto.Response;
import com.example.xiaohui.entity.User;
import com.example.xiaohui.service.BigModel;
import com.example.xiaohui.service.JdkBigModel;
import com.example.xiaohui.service.UserService;
import com.example.xiaohui.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.xiaohui.entity.Msg;
import com.example.xiaohui.entity.UserLoginDTO;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BigModel bigModel;

    @Autowired
    private JdkBigModel jdkBigModel;

//CORS需要改为jsonn
//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password) {
//        // 从服务中获取用户名对应的用户（假设 UserService 实现了获取用户的逻辑）
//        User user = userService.getUsername(username);
//
//        // 如果用户存在
//        if (user != null) {
//            // 比较数据库中存储的密码和输入的密码
//            if (user.getPassword().equals(password)) {
//                // 密码匹配，生成 JWT token（假设 JwtUtil 是一个工具类）
//                return JwtUtil.generateToken(user.getUsername());
//            } else {
//                // 密码不正确
//                return "Invalid password";
//            }
//        } else {
//            // 用户不存在
//            return "User not found";
//        }
//    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDTO loginDTO) {
        User user = userService.getUsername(loginDTO.getUsername());
        if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
            return JwtUtil.generateToken(user.getUsername());
        } else {
            return "Invalid username or password";
        }
    }

//    用http的方式调用
//    @PostMapping("/chatAi")
//    @ResponseBody
//    public Msg chatAi(@RequestBody Msg msg) {
//        String userInput = msg.getMsg(); // 获取用户输入
//        String response = bigModel.chatWithBigModel(userInput); // 调用 service
//
//        // 将响应内容重新封装成 Msg 返回（你也可以只返回部分内容）
//        return new Msg(response);
//    }

    //用JDK的方式调用
    @PostMapping("/chatAi")
    @ResponseBody
    public Msg chatAi(@RequestBody Msg msg) {
        String userInput = msg.getMsg();
        String response = jdkBigModel.chat(userInput);  // 调用 JdkBigModel 中的 chat 方法
        return new Msg(response);
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
    //自动验证了可以不需要这样手动检测
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


    //获取id为1400000的用户信息
    //  127.0.0.1:8888/api/id/2
    @GetMapping("/id/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);  // 返回的是单个 User
    }


    //查询参数的格式   127.0.0.1:8888/api/idd?id=2
    @GetMapping("/idd")
    public User getUserById2(@RequestParam("id") Long id) {
        return userService.getUserById(id);
    }


    //用json格式
    //127.0.0.1:8888/api/iddd
    /***
     *
     * @param user
     * @return
     */
    @PostMapping("/iddd")
    @ResponseBody
    public User getUserById3(@RequestBody User user) {
        Long id = Long.valueOf(user.getId());  // 这里 user.getId() 已经是 Long 类型了
        return userService.getUserById(id);
    }





}
