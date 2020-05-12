package com.jsong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 2020/5/12 14:28
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@EnableAsync
@SpringBootApplication
public class JPayApplication {


    public static void main(String[] args) {
        SpringApplication.run(JPayApplication.class);
    }


}
