package com.example.xiaohui.config;

import com.example.xiaohui.interceptor.JWTInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 将拦截器注入 Spring 容器
     *
     * @return JWTInterceptor 实例
     */
    @Bean
    public JWTInterceptor jwtInterceptor() {
        return new JWTInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns(
                        "/api/login",                  // 登录接口
                        "/api/verify",                 // 验证 token 接口
                        "/api/id/**",
                        "/api/idd",
                        "/api/iddd",
                        "/api/users",                  // 获取用户列表接口
                        "/swagger-resources/**",       // Swagger 相关资源
                        "/webjars/**",                 // 静态资源
                        "/v2/**",
                        "/swagger-ui.html/**"
                );
    }
}
