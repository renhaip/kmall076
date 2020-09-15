package com.kgc.kmall;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan("com.kgc.kmall.managerservice.mapper")
@SpringBootApplication
public class KmallManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallManagerServiceApplication.class, args);
    }

}
