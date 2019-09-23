package com.cl.mybatisplusdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;


@ServletComponentScan
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.cl")
@MapperScan("com.cl.mybatisplusdemo.dao*")
public class MybatisplusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisplusDemoApplication.class, args);
    }
}
