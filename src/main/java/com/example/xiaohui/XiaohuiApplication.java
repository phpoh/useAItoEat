package com.example.xiaohui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author : [hurry]
 * @version : [v1.0]
 * &#064;description  : [启动类]
 * &#064;createTime  : [2025/11/18 10:34]
 */
@SpringBootApplication // Spring Boot 应用
@MapperScan("com.example.xiaohui.mapper")
public class XiaohuiApplication {

	public static void main(String[] args) {
        // 启动 Spring Boot 应用
		SpringApplication.run(XiaohuiApplication.class, args);
	}

}
