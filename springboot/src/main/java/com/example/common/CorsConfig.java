package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); //设置访问源地址
        config.addAllowedHeader("*"); //设置访问源请求头
        config.addAllowedMethod("*"); // 设置访问源请求方法
        source.registerCorsConfiguration("/**", config); //对接口配置跨域
        return new CorsFilter(source);
    }
}
