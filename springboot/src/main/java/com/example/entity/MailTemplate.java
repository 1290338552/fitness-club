package com.example.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 邮件模板实体类
 */
@Data
public class MailTemplate {
    private Integer id;
    private String type; // 模板类型：REMINDER(提醒), NOTIFICATION(通知)
    private String subject; // 邮件主题模板
    private String content; // 邮件内容模板
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}