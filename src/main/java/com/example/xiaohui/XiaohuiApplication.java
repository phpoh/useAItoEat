package com.example.xiaohui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 开启定时任务
@SpringBootApplication // Spring Boot 应用
public class XiaohuiApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiaohuiApplication.class, args); // 启动 Spring Boot 应用
	}

}
