package com.jsong.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 2020/5/13 16:06
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@SpringBootApplication
@MapperScan(basePackages = "com.jsong.mall.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class);
    }
}
