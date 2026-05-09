package com.example.controller;

import com.example.common.Result;
import com.example.entity.Mail;
import com.example.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quick-mail-test")
public class QuickMailTestController {

    @Autowired
    private MailService mailService;

    /**
     * 快速测试邮件发送
     */
    @GetMapping("/send")
    public Result quickTest(@RequestParam(defaultValue = "1290338552@qq.com") String toEmail) {
        try {
            System.out.println("=== 快速邮件测试开始 ===");
            System.out.println("收件人: " + toEmail);
            
            Mail mail = new Mail();
            mail.setFrom("19978018409@163.com");
            mail.setTo(toEmail);
            mail.setSubject("【测试】邮件配置测试 - " + System.currentTimeMillis());
            mail.setContent("这是一封测试邮件，用于验证163邮箱配置是否正常。\n\n" +
                          "发送时间: " + new java.util.Date() + "\n" +
                          "如果您收到这封邮件，说明邮件配置成功！");
            
            mailService.sendSimpleMailMessage(mail);
            
            System.out.println("✅ 测试邮件发送成功！");
            return Result.success("测试邮件发送成功，请检查邮箱: " + toEmail);
            
        } catch (Exception e) {
            System.err.println("❌ 测试邮件发送失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("邮件发送失败: " + e.getMessage());
        }
    }

    /**
     * 检查邮件配置信息
     */
    @GetMapping("/config")
    public Result checkConfig() {
        try {
            System.out.println("=== 当前邮件配置信息 ===");
            System.out.println("SMTP服务器: smtp.163.com");
            System.out.println("端口: 465 (SSL)");
            System.out.println("发件邮箱: 19978018409@163.com");
            System.out.println("授权码: PM6XgmDqPyUp7ntw");
            System.out.println("SSL启用: true");
            System.out.println("调试模式: true");
            
            return Result.success("邮件配置信息已输出到控制台");
        } catch (Exception e) {
            return Result.error("获取配置失败: " + e.getMessage());
        }
    }
}