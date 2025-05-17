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
        // 1. 放行OPTIONS请求，避免CORS预检失败
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false; // 不继续后续拦截器链
        }

        // 2. 从请求头获取token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        token = token.substring(7); // 去掉 Bearer 前缀

        try {
            // 3. 验证 token 是否有效（使用 JwtUtil 工具）
            JwtUtil.verifyToken(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true; // 验证通过
    }

}
