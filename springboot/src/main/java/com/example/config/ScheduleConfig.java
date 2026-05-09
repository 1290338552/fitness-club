package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置类
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {
    // Spring Boot会自动扫描@Scheduled注解的方法
}