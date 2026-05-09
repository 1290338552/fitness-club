package com.example.service;


import com.example.entity.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMailMessage(Mail mail) {
        System.out.println("待发送邮件信息：" + mail); // 打印参数（调试用）
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        // 1. 发件人非空校验
        if (!StringUtils.hasText(mail.getFrom())) {
            throw new IllegalArgumentException("发送人邮箱不能为空");
        }
        simpleMailMessage.setFrom(mail.getFrom());

        // 2. 收件人非空校验 + 拆分（split参数是英文逗号）
        if (!StringUtils.hasText(mail.getTo())) {
            throw new IllegalArgumentException("收件人邮箱不能为空");
        }
        simpleMailMessage.setTo(mail.getTo().split(","));

        // 3. 抄送人（可选）
        if (StringUtils.hasText(mail.getCc())) {
            simpleMailMessage.setCc(mail.getCc().split(","));
        }

        // 4. 主题/内容非空校验
        if (!StringUtils.hasText(mail.getSubject())) {
            throw new IllegalArgumentException("邮件主题不能为空");
        }
        if (!StringUtils.hasText(mail.getContent())) {
            throw new IllegalArgumentException("邮件内容不能为空");
        }
        simpleMailMessage.setSubject(mail.getSubject());
        simpleMailMessage.setText(mail.getContent());

        // 发送邮件
        javaMailSender.send(simpleMailMessage);
        System.out.println("邮件发送成功！");
    }
}
