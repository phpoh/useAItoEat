package com.example.xiaohui.interceptor;

import com.example.xiaohui.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;



@Component
public class JWTInterceptor implements HandlerInterceptor {

    /**
     * 拦截请求并验证 JWT
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取 token
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            // 没有 token，直接拦截请求
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token is missing or invalid");
            return false;
        }

        // 解析并验证 token 的逻辑
        try {
            // 假设有一个工具类 JwtUtil 来验证和解析 token
            boolean isValid = JwtUtil.verifyToken(token); // 替换为你的实际逻辑
            if (!isValid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is invalid");
                return false;
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error validating token");
            return false;
        }

        // token 验证通过，允许请求继续处理
        return true;
    }
}
