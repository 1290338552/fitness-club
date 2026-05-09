package com.example.controller;



import com.example.entity.Mail;
import com.example.service.MailService;
import com.example.common.Result; // 确保Result类存在（返回统一格式）
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mail") // 接口前缀：/mail
public class MailController {

    @Autowired
    private MailService mailService;

    // 接口路径：/mail/sendSimpleMail（直接用Mail实体类接收表单参数）
    @PostMapping("/sendSimpleMail")
    public Result sendSimpleMail(Mail mail) {
        try {
            System.out.println("后端接收的Mail：" + mail); // 打印验证
            mailService.sendSimpleMailMessage(mail);
            return Result.success("邮件发送成功");
        } catch (IllegalArgumentException e) {
            // 参数校验异常：返回友好提示
            return Result.error(e.getMessage());
        } catch (Exception e) {
            // 其他异常（如邮箱配置错误）
            e.printStackTrace(); // 打印详细异常
            return Result.error("系统异常：" + e.getMessage());
        }
    }
}