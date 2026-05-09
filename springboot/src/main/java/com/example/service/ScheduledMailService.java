package com.example.service;

import com.example.entity.*;
import com.example.entity.Record;
import com.example.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledMailService {
    
    @Autowired
    private ScheduledMailMapper scheduledMailMapper;
    
    @Autowired
    private MailTemplateMapper mailTemplateMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private CoachMapper coachMapper;
    
    @Autowired
    private RecordMapper recordMapper;
    
    @Autowired
    private MailService mailService;
    
    /**
     * 创建定时邮件任务（通用方法）
     * @param userId 用户ID
     * @param recordId 预约记录ID（可为null）
     * @param toEmail 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param delayMinutes 延迟发送分钟数
     */
    @Transactional
    public void createScheduledMail(Integer userId, Integer recordId, String toEmail, 
                                  String subject, String content, int delayMinutes) {
        try {
            ScheduledMail scheduledMail = new ScheduledMail();
            scheduledMail.setUserId(userId);
            scheduledMail.setRecordId(recordId);
            scheduledMail.setToEmail(toEmail);
            scheduledMail.setSubject(subject);
            scheduledMail.setContent(content);
            scheduledMail.setScheduledTime(LocalDateTime.now().plusMinutes(delayMinutes));
            scheduledMail.setStatus("PENDING");
            scheduledMail.setSentCount(0);
            scheduledMail.setMaxSendCount(2);
            // 如果延迟为0，立即发送；否则按延迟时间发送
            scheduledMail.setNextSendTime(delayMinutes == 0 ? LocalDateTime.now() : LocalDateTime.now().plusMinutes(delayMinutes));
            scheduledMail.setCreateTime(LocalDateTime.now());
            
            scheduledMailMapper.insert(scheduledMail);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建定时邮件失败: " + e.getMessage());
        }
    }

    /**
     * 创建预约提醒邮件任务
     * @param recordId 预约记录ID
     */
    @Transactional
    public void createReminderMail(Integer recordId) {
        try {
            // 获取预约记录
            Record record = recordMapper.selectById(recordId);
            if (record == null || !"预约成功".equals(record.getStatus())) {
                return;
            }
            
            // 获取用户信息
            User user = userMapper.selectById(record.getUserId());
            if (user == null || user.getEmail() == null) {
                return;
            }
            
            // 获取教练信息
            Coach coach = coachMapper.selectById(record.getCoachId());
            if (coach == null) {
                return;
            }
            
            // 获取课程信息（如果有课程ID）
            String courseName = "个人训练";
            String courseDescription = "个人定制训练课程";
            String duration = "60";
            
            if (record.getCourseId() != null) {
                // 这里需要添加CourseMapper的查询，暂时使用默认值
                courseName = record.getCourseName() != null ? record.getCourseName() : "个人训练";
                courseDescription = record.getCourseDescription() != null ? record.getCourseDescription() : "个人定制训练课程";
                duration = record.getCourseDuration() != null ? record.getCourseDuration().toString() : "60";
            }
            
            // 获取邮件模板
            MailTemplate template = mailTemplateMapper.selectByType("REMINDER");
            if (template == null) {
                // 使用默认模板
                template = createDefaultReminderTemplate();
            }
            
            // 替换模板变量
            String subject = template.getSubject()
                .replace("{userName}", user.getName())
                .replace("{coachName}", coach.getCoach())
                .replace("{courseName}", courseName);
                
            String content = template.getContent()
                .replace("{userName}", user.getName())
                .replace("{coachName}", coach.getCoach())
                .replace("{coachPosition}", coach.getName())
                .replace("{price}", coach.getPrice().toString())
                .replace("{appointmentTime}", record.getTime())
                .replace("{courseName}", courseName)
                .replace("{courseDescription}", courseDescription)
                .replace("{duration}", duration);
            
            // 解析预约时间字符串并创建定时邮件任务（立即发送第一次）
            LocalDateTime appointmentTime;
            try {
                // 假设时间格式为 "yyyy-MM-dd HH:mm:ss"
                appointmentTime = LocalDateTime.parse(record.getTime().replace(" ", "T"));
            } catch (Exception e) {
                // 如果解析失败，使用当前时间+1天作为默认值
                appointmentTime = LocalDateTime.now().plusDays(1);
                System.err.println("解析预约时间失败，使用默认时间: " + e.getMessage());
            }
            
            ScheduledMail scheduledMail = new ScheduledMail();
            scheduledMail.setUserId(user.getId());
            scheduledMail.setRecordId(recordId);
            scheduledMail.setToEmail(user.getEmail());
            scheduledMail.setSubject(subject);
            scheduledMail.setContent(content);
            scheduledMail.setScheduledTime(LocalDateTime.now()); // 立即发送
            scheduledMail.setStatus("PENDING");
            scheduledMail.setSentCount(0); // 初始发送次数为0
            scheduledMail.setMaxSendCount(2); // 最大发送2次
            scheduledMail.setNextSendTime(LocalDateTime.now()); // 立即开始第一次发送
            scheduledMail.setCreateTime(LocalDateTime.now());
            
            scheduledMailMapper.insert(scheduledMail);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建提醒邮件失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理待发送的邮件 - 每30秒发送一次，共发送2次
     */
    public void processPendingMails() {
        List<ScheduledMail> pendingMails = scheduledMailMapper.selectPendingMails(LocalDateTime.now());
        
        for (ScheduledMail scheduledMail : pendingMails) {
            try {
                // 发送邮件
                Mail mail = new Mail();
                mail.setFrom("19978018409@163.com"); // 使用配置文件中的发件人
                mail.setTo(scheduledMail.getToEmail());
                
                // 根据发送次数调整邮件主题和内容
                int currentSendCount = scheduledMail.getSentCount() + 1;
                String subject = scheduledMail.getSubject();
                String content = scheduledMail.getContent();
                
                if (currentSendCount == 1) {
                    subject = "【第1次提醒】" + subject;
                    content = "【第一次提醒】\n\n" + content + "\n\n注意：我们将在30秒后再次提醒您。";
                } else if (currentSendCount == 2) {
                    subject = "【第2次提醒】" + subject;
                    content = "【最后一次提醒】\n\n" + content + "\n\n这是最后一次提醒，请务必准时参加！";
                }
                
                mail.setSubject(subject);
                mail.setContent(content);
                
                mailService.sendSimpleMailMessage(mail);
                
                // 更新发送状态
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime nextSendTime = null;
                String status = "PENDING";
                
                // 如果还没达到最大发送次数，计算下次发送时间
                if (currentSendCount < scheduledMail.getMaxSendCount()) {
                    nextSendTime = now.plusSeconds(30); // 30秒后再次发送
                    status = "PENDING";
                } else {
                    status = "COMPLETED"; // 已完成所有发送
                }
                
                scheduledMailMapper.updateAfterSend(
                    scheduledMail.getId(), 
                    status, 
                    currentSendCount,
                    now, 
                    nextSendTime, 
                    null
                );
                
                System.out.println("课程提醒邮件发送成功 (第" + currentSendCount + "次): " + scheduledMail.getToEmail());
                
            } catch (Exception e) {
                // 发送失败，记录错误但不增加发送次数
                scheduledMailMapper.updateAfterSend(
                    scheduledMail.getId(), 
                    "FAILED", 
                    scheduledMail.getSentCount(),
                    null, 
                    LocalDateTime.now().plusSeconds(30), // 30秒后重试
                    e.getMessage()
                );
                
                System.err.println("课程提醒邮件发送失败: " + scheduledMail.getToEmail() + 
                    ", 错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 创建默认提醒模板
     */
    private MailTemplate createDefaultReminderTemplate() {
        MailTemplate template = new MailTemplate();
        template.setSubject("【健身房】课程提醒 - {courseName}");
        template.setContent("亲爱的{userName}，\n\n" +
            "您好！这是来自健身房的课程提醒。\n\n" +
            "您预约的课程信息如下：\n" +
            "课程名称：{courseName}\n" +
            "课程描述：{courseDescription}\n" +
            "教练：{coachName}（{coachPosition}）\n" +
            "课程费用：￥{price}\n" +
            "课程时长：{duration}分钟\n" +
            "预约时间：{appointmentTime}\n\n" +
            "请您准时参加课程，如有任何问题请及时联系我们。\n\n" +
            "祝您健身愉快！\n" +
            "健身房管理团队");
        return template;
    }
}