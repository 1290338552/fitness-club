package com.example.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 定时邮件任务实体类
 */
@Data
public class ScheduledMail {
    private Integer id;
    private Integer userId; // 用户ID
    private Integer recordId; // 预约记录ID
    private String toEmail; // 收件人邮箱
    private String subject; // 邮件主题
    private String content; // 邮件内容
    private LocalDateTime scheduledTime; // 计划发送时间
    private String status; // 状态：PENDING(待发送), SENT(已发送), FAILED(发送失败), COMPLETED(已完成)
    private Integer sentCount; // 已发送次数
    private Integer maxSendCount; // 最大发送次数
    private LocalDateTime lastSentTime; // 最后发送时间
    private LocalDateTime nextSendTime; // 下次发送时间
    private String errorMessage; // 错误信息
    private LocalDateTime createTime; // 创建时间
}