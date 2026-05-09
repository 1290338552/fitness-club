package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//配置类Configuration
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns( "/login",
                        "/register",
                        "/files/download/**",
                        "/files/upload",      // 添加文件上传路径
                        "/files/test/**",     // 添加测试接口
                        "/course/test/**",    // 添加课程测试接口
                        "/debug/email/**",    // 添加邮件调试接口
                        "/quick-mail-test/**", // 添加快速邮件测试接口
                        "/api/sms/**",
                        "/verify-code",
                        "/send-code",
                        "/phone",
                        "/pay/notify",
                        "/ai/chat",
                        "/ai/applyAdvice/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源访问路径
        String filePath = System.getProperty("user.dir") + "/files/";
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + filePath);
        
        System.out.println("静态资源路径配置：" + filePath);
    }

    @Bean
    public JWTInterceptor  jwtInterceptor() {
        return new JWTInterceptor();
    }
}
