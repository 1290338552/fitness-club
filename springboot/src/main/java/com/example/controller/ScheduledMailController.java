package com.example.controller;

import com.example.common.Result;
import com.example.entity.ScheduledMail;
import com.example.service.ScheduledMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 定时邮件管理控制器
 */
@RestController
@RequestMapping("/scheduledMail")
public class ScheduledMailController {
    
    @Autowired
    private ScheduledMailService scheduledMailService;
    
    /**
     * 手动触发邮件发送（测试用）
     */
    @PostMapping("/triggerSend")
    public Result triggerSend() {
        try {
            scheduledMailService.processPendingMails();
            return Result.success("邮件发送任务已触发");
        } catch (Exception e) {
            return Result.error("触发邮件发送失败: " + e.getMessage());
        }
    }
    
    /**
     * 为指定预约记录创建提醒邮件（测试用）
     */
    @PostMapping("/createReminder/{recordId}")
    public Result createReminder(@PathVariable Integer recordId) {
        try {
            scheduledMailService.createReminderMail(recordId);
            return Result.success("提醒邮件任务创建成功");
        } catch (Exception e) {
            return Result.error("创建提醒邮件失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建测试邮件任务（30秒间隔测试）
     */
    @PostMapping("/createTestMail")
    public Result createTestMail(@RequestParam String email) {
        try {
            scheduledMailService.createScheduledMail(
                null, // userId
                null, // recordId
                email, // toEmail
                "【测试】30秒定时邮件测试", // subject
                "这是一个30秒间隔的定时邮件测试，系统将发送2次邮件，间隔30秒。", // content
                0 // 立即发送
            );
            return Result.success("测试邮件任务创建成功，将在30秒内发送2次邮件");
        } catch (Exception e) {
            return Result.error("创建测试邮件失败: " + e.getMessage());
        }
    }
}