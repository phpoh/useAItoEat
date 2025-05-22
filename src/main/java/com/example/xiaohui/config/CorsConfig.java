package com.example.xiaohui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://127.0.0.1:5173",
                        "https://localhost:5173",
                        "https://192.168.43.50:5173",
                        "https://192.168.112.1:5173",
                        "http://113.45.179.101",
                        "https://113.45.179.101",
                        "http://113.45.179.101:80",
                        "https://113.45.179.101:80",
                        "http://113.45.179.101:443",
                        "https://113.45.179.101:443",
                        "http://www.elonmusk.group",
                        "https://www.elonmusk.group",
                        "http://wangzhihui.elonmusk.group",
                        "https://wangzhihui.elonmusk.group",
                        "http://www.aboutwangzhihui.top",
                        "https://www.aboutwangzhihui.top"
                ) // 指定前端地址注意前端是https!!!!!!!!!
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // 允许携带认证信息（如 JWT Token）
                .maxAge(3600);
    }
}
