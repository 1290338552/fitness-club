package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 邮件定时任务执行器
 */
@Service
public class MailScheduleService {
    
    @Autowired
    private ScheduledMailService scheduledMailService;
    
    /**
     * 每30秒检查一次待发送的邮件
     * 每个预约会发送2次提醒邮件，间隔30秒
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void processScheduledMails() {
        System.out.println("开始处理定时邮件任务（每30秒发送一次，共2次）...");
        try {
            scheduledMailService.processPendingMails();
            System.out.println("定时邮件任务处理完成");
        } catch (Exception e) {
            System.err.println("定时邮件任务处理失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 每天凌晨1点清理7天前的已发送邮件记录（可选）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanupOldMailRecords() {
        System.out.println("开始清理旧邮件记录...");
        // 这里可以添加清理逻辑，删除7天前的已发送邮件记录
        System.out.println("旧邮件记录清理完成");
    }
}