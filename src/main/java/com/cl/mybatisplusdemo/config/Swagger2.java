package com.cl.mybatisplusdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.ApiInfo;

/**
 * @author cl
 * @version V1.0
 * @Package com.groot.store
 * @Description:
 * @Date: 2018-11-27 14:01
 */
@Configuration
@EnableSwagger2
public class Swagger2 implements WebMvcConfigurer {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("com.cl.mybatisplusdemo.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("swagger-api文档")
                .description("swagger接入教程")
                .termsOfServiceUrl("https://www.baidu.com")
                .version("1.0").build();
    }

}
