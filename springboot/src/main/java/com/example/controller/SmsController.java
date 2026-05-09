package com.example.controller;

import com.example.common.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信验证码控制器
 * 提供RESTful API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private com.example.service.SmsService smsService;

    /**
     * 发送验证码接口
     * POST /api/sms/send-code
     */
    @PostMapping("/send-code")
    public Result sendCode(@RequestBody Map<String, String> request) { // 返回值改为Result
        String phoneNumber = request.get("phoneNumber");// 从请求体中获取手机号
        log.info("收到发送验证码请求，手机号: {}", phoneNumber);

        // 校验手机号非空
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }

        return smsService.sendVerificationCode(phoneNumber);// 调用发送服务（返回Result）
    }

    /**
     * 验证验证码接口
     * POST /api/sms/verify-code
     */
    @PostMapping("/verify-code")
    public Result verifyCode(@RequestBody Map<String, String> request) { // 返回值改为Result
        String phoneNumber = request.get("phoneNumber");// 获取手机号
        String code = request.get("code");// 获取验证码
        log.info("收到验证验证码请求，手机号: {}, 验证码: {}", phoneNumber, code);

        // 校验参数非空
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return Result.error("手机号不能为空");
        }
        if (code == null || code.trim().isEmpty()) {
            return Result.error("验证码不能为空");
        }

        return smsService.verifyCode(phoneNumber, code);// 调用验证服务（返回Result）
    }

    /**
     * 健康检查接口
     * 用于测试服务是否正常运行
     */
    @GetMapping("/health")
    public Result health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("service", "SMS Verification Service");
        data.put("timestamp", System.currentTimeMillis());


        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "服务正常运行");
        responseData.put("data", data);
        return Result.success(responseData);

    }
}