package com.example.controller;

import com.example.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

/**
 * 邮件测试控制器
 */
@RestController
@RequestMapping("/mailTest")
public class MailTestController {
    
    @Autowired
    private JavaMailSender mailSender;
    
    /**
     * 测试邮件发送功能
     */
    @PostMapping("/sendTest")
    public Result sendTestMail(@RequestParam String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("19978018409@163.com"); // 发件人
            message.setTo(toEmail); // 收件人
            message.setSubject("【测试】邮件配置测试");
            message.setText("这是一封测试邮件，用于验证邮件配置是否正确。\n\n如果您收到这封邮件，说明邮件服务配置成功！\n\n发送时间：" + new java.util.Date());
            
            System.out.println("开始发送测试邮件到：" + toEmail);
            mailSender.send(message);
            System.out.println("测试邮件发送成功！");
            
            return Result.success("测试邮件发送成功！请检查邮箱：" + toEmail);
        } catch (Exception e) {
            System.err.println("邮件发送失败：" + e.getMessage());
            e.printStackTrace();
            return Result.error("邮件发送失败：" + e.getMessage());
        }
    }
    
    /**
     * 手动测试预约成功邮件发送
     */
    @PostMapping("/testBookingSuccess")
    public Result testBookingSuccessMail(@RequestParam Integer userId, @RequestParam String toEmail) {
        try {
            // 构建测试邮件内容
            String subject = "【测试】预约成功通知";
            String content = "这是一封测试预约成功邮件。\n\n如果您收到这封邮件，说明预约成功邮件功能正常！\n\n发送时间：" + new java.util.Date();
            
            // 使用MailService发送邮件
            com.example.entity.Mail mail = new com.example.entity.Mail();
            mail.setFrom("19978018409@163.com");
            mail.setTo(toEmail);
            mail.setSubject(subject);
            mail.setContent(content);
            
            System.out.println("开始发送预约成功测试邮件到：" + toEmail);
            
            // 这里需要注入MailService
            // mailService.sendSimpleMailMessage(mail);
            
            // 先用SimpleMailMessage测试
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("19978018409@163.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            System.out.println("预约成功测试邮件发送成功！");
            
            return Result.success("预约成功测试邮件发送成功！请检查邮箱：" + toEmail);
        } catch (Exception e) {
            System.err.println("预约成功测试邮件发送失败：" + e.getMessage());
            e.printStackTrace();
            return Result.error("邮件发送失败：" + e.getMessage());
        }
    }
}