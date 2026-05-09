package com.example.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.example.common.AliyunSmsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SmsService {

    @Autowired
    private IAcsClient acsClient;

    @Autowired
    private AliyunSmsConfig smsConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 本地缓存存储验证码信息
    private final Map<String, Map<String, String>> codeCache = new ConcurrentHashMap<>();

    // 验证码有效期（5分钟）
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000;

    // 验证码发送频率限制（60秒）
    private static final long SEND_INTERVAL = 60 * 1000;
    private final Map<String, Long> sendTimeCache = new ConcurrentHashMap<>();

    /**
     * 发送短信验证码
     * @param phoneNumber 手机号码
     * @return 发送结果
     */
    public Result sendVerificationCode(String phoneNumber) {
        try {
            // 检查发送频率
            Long lastSendTime = sendTimeCache.get(phoneNumber);
            if (lastSendTime != null && System.currentTimeMillis() - lastSendTime < SEND_INTERVAL) {
                return Result.error("发送过于频繁，请稍后再试");
            }

            log.info("发送验证码到: {}", phoneNumber);

            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dypnsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("SendSmsVerifyCode");

            request.putQueryParameter("PhoneNumber", phoneNumber);
            request.putQueryParameter("SignName", smsConfig.getSignName());
            request.putQueryParameter("TemplateCode", smsConfig.getTemplateCode());
            request.putQueryParameter("TemplateParam", "{\"code\":\"##code##\",\"min\":\"5\"}");
            request.putQueryParameter("ReturnVerifyCode", "true");

            CommonResponse response = acsClient.getCommonResponse(request);
            String responseData = response.getData();
            log.info("发送验证码响应: {}", responseData);

            Map<String, Object> result = objectMapper.readValue(responseData, Map.class);

            if ("OK".equals(result.get("Code"))) {
                // 提取阿里云返回的验证码和BizId
                Map<String, Object> model = (Map<String, Object>) result.get("Model");
                String verifyCode = (String) model.get("VerifyCode");
                String bizId = (String) model.get("BizId");

                // 存储验证码信息到本地缓存
                Map<String, String> cacheData = new HashMap<>();
                cacheData.put("code", verifyCode);
                cacheData.put("bizId", bizId);
                cacheData.put("createTime", String.valueOf(System.currentTimeMillis()));
                codeCache.put(phoneNumber, cacheData);

                // 记录发送时间（频率限制）
                sendTimeCache.put(phoneNumber, System.currentTimeMillis());

                log.info("验证码存储成功: 手机号={}, 验证码={}, BizId={}", phoneNumber, verifyCode, bizId);
                return Result.success("验证码发送成功");
            } else {
                log.error("发送验证码失败: {}", result.get("Message"));
                return Result.error("发送失败: " + result.get("Message"));
            }

        } catch (Exception e) {
            log.error("发送短信验证码异常: ", e);
            return Result.error("发送失败: " + e.getMessage());
        }
    }

    /**
     * 验证短信验证码
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return 验证结果
     */
    public Result verifyCode(String phoneNumber, String code) {
        try {
            log.info("开始核验验证码: 手机号={}, 输入的验证码={}", phoneNumber, code);

            // 1. 本地缓存校验
            Map<String, String> cacheData = codeCache.get(phoneNumber);
            if (cacheData == null) {
                log.warn("验证码不存在，手机号: {}, 缓存大小: {}", phoneNumber, codeCache.size());
                return Result.error("验证码不存在，请重新获取");
            }

            String storedCode = cacheData.get("code");
            String bizId = cacheData.get("bizId");
            long createTime = Long.parseLong(cacheData.get("createTime"));

            log.info("缓存中的验证码: 手机号={}, 存储的验证码={}, 过期时间={}ms",
                    phoneNumber, storedCode, CODE_EXPIRE_TIME);

            // 检查验证码是否过期
            long currentTime = System.currentTimeMillis();
            long timePassed = currentTime - createTime;

            if (timePassed > CODE_EXPIRE_TIME) {
                log.warn("验证码已过期: 手机号={}, 创建时间={}, 当前时间={}, 经过时间={}ms",
                        phoneNumber, createTime, currentTime, timePassed);
                codeCache.remove(phoneNumber);
                return Result.error("验证码已过期，请重新获取");
            }

            // 检查验证码是否匹配（去除空格，忽略大小写）
            if (storedCode == null || code == null) {
                log.error("验证码为空: storedCode={}, inputCode={}", storedCode, code);
                return Result.error("验证码不能为空");
            }

            // 去除空格，转为字符串比较
            String trimmedStoredCode = storedCode.trim();
            String trimmedInputCode = code.trim();

            log.info("比较验证码: 存储='{}', 输入='{}'", trimmedStoredCode, trimmedInputCode);

            if (!trimmedStoredCode.equals(trimmedInputCode)) {
                log.warn("验证码不匹配: 期望={}, 实际={}", trimmedStoredCode, trimmedInputCode);
                return Result.error("验证码错误，请重新输入");
            }

            // 2. 阿里云接口验证
            log.info("本地验证通过，开始调用阿里云验证接口");

            CommonRequest request = new CommonRequest();
            request.setSysMethod(MethodType.POST);
            request.setSysDomain("dypnsapi.aliyuncs.com");
            request.setSysVersion("2017-05-25");
            request.setSysAction("CheckSmsVerifyCode");

            // 完整参数
            request.putQueryParameter("PhoneNumber", phoneNumber);
            request.putQueryParameter("VerifyCode", trimmedInputCode);
            request.putQueryParameter("BizId", bizId);

            log.info("调用阿里云核验接口: PhoneNumber={}, VerifyCode={}, BizId={}",
                    phoneNumber, trimmedInputCode, bizId);

            CommonResponse response = acsClient.getCommonResponse(request);
            String responseData = response.getData();
            log.info("阿里云核验响应: {}", responseData);

            Map<String, Object> result = objectMapper.readValue(responseData, Map.class);
            log.info("阿里云核验结果解析: {}", result);

            if (Boolean.TRUE.equals(result.get("Success")) || "OK".equals(result.get("Code"))) {
                // 验证成功，清除缓存
                codeCache.remove(phoneNumber);
                sendTimeCache.remove(phoneNumber);
                log.info("验证码验证成功: 手机号={}", phoneNumber);
                return Result.success("验证成功");
            } else {
                String errorMsg = (String) result.getOrDefault("Message", "验证失败");
                log.error("阿里云验证失败: {}", errorMsg);
                return Result.error("验证失败: " + errorMsg);
            }

        } catch (Exception e) {
            log.error("核验验证码异常: ", e);
            return Result.error("验证失败: " + e.getMessage());
        }
    }

    /**
     * 调试方法：查看当前缓存中的验证码
     */
    public Map<String, Map<String, String>> getCodeCache() {
        return new HashMap<>(codeCache);
    }

    /**
     * 调试方法：清除指定手机的验证码
     */
    public void clearCodeCache(String phoneNumber) {
        codeCache.remove(phoneNumber);
        sendTimeCache.remove(phoneNumber);
        log.info("已清除手机号 {} 的验证码缓存", phoneNumber);
    }

    /**
     * 清理过期验证码
     */
    public void cleanExpiredCodes() {
        long now = System.currentTimeMillis();
        int beforeSize = codeCache.size();

        codeCache.entrySet().removeIf(entry -> {
            long createTime = Long.parseLong(entry.getValue().get("createTime"));
            return now - createTime > CODE_EXPIRE_TIME;
        });

        sendTimeCache.entrySet().removeIf(entry -> now - entry.getValue() > SEND_INTERVAL * 5);

        int afterSize = codeCache.size();
        log.info("过期验证码清理完成: 清理前 {} 条, 清理后 {} 条", beforeSize, afterSize);
    }
}