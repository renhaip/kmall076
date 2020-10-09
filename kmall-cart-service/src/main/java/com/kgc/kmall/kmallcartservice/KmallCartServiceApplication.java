package com.kgc.kmall.kmallcartservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.kgc.kmall.kmallcartservice.mapper")
@SpringBootApplication
public class KmallCartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmallCartServiceApplication.class, args);
    }

}
