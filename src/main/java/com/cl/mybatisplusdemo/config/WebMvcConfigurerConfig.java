package com.cl.mybatisplusdemo.config;

/**
 * @author cl
 * @version V1.0
 */

import cn.hutool.http.Method;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(basePackages= "com.cl.mybatisplusdemo")
@EnableWebMvc
public class WebMvcConfigurerConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        CorsRegistration corsRegistration = registry.addMapping("/**");

            corsRegistration.allowedOrigins("*");

        corsRegistration.allowCredentials(true)
                .allowedMethods(Method.GET.name(), Method.POST.name(), Method.OPTIONS.name())
                .maxAge(3600);
    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("proDetail");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Swagger自带页面处理
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        //静态资源处理
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/WEB-INF/static/")
                .setCacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS).cachePublic());
    }


    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}