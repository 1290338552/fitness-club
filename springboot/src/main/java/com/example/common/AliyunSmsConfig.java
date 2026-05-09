package com.example.common;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;

    // 创建阿里云客户端配置

    @Bean
    public IAcsClient acsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou", // 地域ID
                accessKeyId,
                accessKeySecret
        );
        return new DefaultAcsClient(profile);
    }
}
