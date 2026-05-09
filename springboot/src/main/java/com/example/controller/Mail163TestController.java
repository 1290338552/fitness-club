package com.example.controller;

import com.example.common.Result;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 * 163邮箱专用测试控制器
 */
@RestController
@RequestMapping("/mail163Test")
public class Mail163TestController {
    
    /**
     * 直接使用JavaMail API测试163邮箱
     */
    @PostMapping("/directTest")
    public Result directTest(@RequestParam String toEmail) {
        try {
            // 163邮箱配置
            String host = "smtp.163.com";
            String username = "19978018409@163.com";
            String password = "PM6XgmDqPyUp7ntw"; // 新的授权码
            
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", host);
            props.put("mail.debug", "true");
            
            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            
            // 创建邮件
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("【163邮箱测试】直接API测试");
            message.setText("这是使用JavaMail API直接发送的163邮箱测试邮件。\n\n发送时间：" + new java.util.Date());
            
            // 发送邮件
            System.out.println("开始使用JavaMail API发送163邮件...");
            Transport.send(message);
            System.out.println("163邮件发送成功！");
            
            return Result.success("163邮件发送成功！请检查邮箱：" + toEmail);
            
        } catch (Exception e) {
            System.err.println("163邮件发送失败：" + e.getMessage());
            e.printStackTrace();
            return Result.error("163邮件发送失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试不同端口配置
     */
    @PostMapping("/testPorts")
    public Result testPorts(@RequestParam String toEmail) {
        String[] ports = {"25", "465", "587", "994"};
        String username = "19978018409@163.com";
        String password = "PM6XgmDqPyUp7ntw";
        
        for (String port : ports) {
            try {
                System.out.println("测试端口：" + port);
                
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.163.com");
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.auth", "true");
                
                if ("465".equals(port) || "994".equals(port)) {
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.ssl.trust", "smtp.163.com");
                } else {
                    props.put("mail.smtp.starttls.enable", "true");
                }
                
                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
                
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject("【163测试】端口" + port + "测试");
                message.setText("端口" + port + "测试成功！");
                
                Transport.send(message);
                System.out.println("端口 " + port + " 测试成功！");
                
                return Result.success("端口 " + port + " 测试成功！邮件已发送到：" + toEmail);
                
            } catch (Exception e) {
                System.err.println("端口 " + port + " 测试失败：" + e.getMessage());
            }
        }
        
        return Result.error("所有端口测试都失败了");
    }
}