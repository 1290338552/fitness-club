package com.example.entity;


import lombok.Data;

/**
 * 邮件参数实体类
 * 接收前端传递的邮件信息
 */
@Data
public class Mail {
    // 发件人（需和配置文件的username一致）
    private String from;
    // 收件人（多个邮箱用逗号分隔，后端会拆分）
    private String to;
    // 抄送人（多个邮箱用逗号分隔）
    private String cc;
    // 邮件主题
    private String subject;
    // 邮件正文
    private String content;

}
