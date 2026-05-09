package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Apply;
import com.example.entity.Course;
import com.example.entity.User;
import com.example.mapper.ApplyMapper;
import com.example.service.RechargeService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyService {

    @Resource
    ApplyMapper applyMapper;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ScheduledMailService scheduledMailService;
    
    @Autowired
    private MailService mailService;

    @Autowired
    private RechargeService rechargeService;

    public void add(Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        apply.setUserId(currentUser.getId());
        apply.setTime(DateUtil.now());
        apply.setStatus("预约中");
        
        // 如果没有指定课程类型，默认为自定义课程
        if (apply.getCourseType() == null || apply.getCourseType().isEmpty()) {
            apply.setCourseType("CUSTOM");
        }
        
        applyMapper.insert(apply);
    }

    public void update(Apply apply) {
        // 获取更新前的状态
        Apply oldApply = applyMapper.selectById(apply.getId());
        String oldStatus = oldApply.getStatus();
        
        System.out.println("Apply状态更新：" + oldStatus + " -> " + apply.getStatus());
        
        //根据主键修改
        applyMapper.updateById(apply);
        
        // 如果状态从其他状态变为"预约成功"，则扣款并发送邮件
        if (!"预约成功".equals(oldStatus) && "预约成功".equals(apply.getStatus())) {
            // 课程预约需要扣款
            if ("COURSE".equals(oldApply.getCourseType()) && oldApply.getCourseId() != null) {
                Course course = courseService.selectById(oldApply.getCourseId());
                if (course != null && course.getPrice() != null) {
                    boolean ok = rechargeService.consume(
                        oldApply.getUserId(),
                        course.getPrice(),
                        "预约课程：" + course.getName()
                    );
                    if (!ok) {
                        throw new com.example.exception.CustomerException("会员余额不足，无法完成预约");
                    }
                }
            }
            System.out.println("检测到预约成功，开始发送邮件...");
            createEmailReminder(apply);
        }
    }
    
    /**
     * 创建邮件提醒任务
     */
    private void createEmailReminder(Apply apply) {
        try {
            System.out.println("=== 开始创建邮件提醒 ===");
            System.out.println("预约ID: " + apply.getId());
            System.out.println("用户ID: " + apply.getUserId());
            System.out.println("预约状态: " + apply.getStatus());
            
            // 获取用户信息
            User user = userService.selectById(apply.getUserId());
            if (user == null) {
                System.err.println("错误：用户不存在，用户ID: " + apply.getUserId());
                return;
            }
            
            System.out.println("用户姓名: " + user.getName());
            System.out.println("用户邮箱: " + user.getEmail());
            
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                System.err.println("错误：用户邮箱为空，跳过邮件发送");
                return;
            }
            
            String subject;
            String content;
            
            if ("COURSE".equals(apply.getCourseType()) && apply.getCourseId() != null) {
                System.out.println("预约类型: 课程预约，课程ID: " + apply.getCourseId());
                
                // 课程预约邮件
                Course course = courseService.selectById(apply.getCourseId());
                if (course == null) {
                    System.err.println("错误：课程不存在，课程ID: " + apply.getCourseId());
                    return;
                }
                
                System.out.println("课程名称: " + course.getName());
                System.out.println("教练姓名: " + course.getCoachName());
                
                subject = "【健身房】课程预约成功 - " + course.getName();
                content = String.format(
                    "亲爱的%s，\n\n" +
                    "您好！您的课程预约已审核通过。\n\n" +
                    "预约信息如下：\n" +
                    "课程名称：%s\n" +
                    "课程描述：%s\n" +
                    "教练：%s\n" +
                    "课程费用：￥%s\n" +
                    "课程时长：%d分钟\n" +
                    "预约时间：%s\n\n" +
                    "请您准时参加课程，如有任何问题请及时联系我们。\n\n" +
                    "祝您健身愉快！\n" +
                    "健身房管理团队",
                    user.getName(),
                    course.getName(),
                    course.getDescription(),
                    course.getCoachName(),
                    course.getPrice(),
                    course.getDuration(),
                    apply.getTime()
                );
            } else {
                System.out.println("预约类型: 自定义课程");
                System.out.println("课程标题: " + apply.getTitle());
                
                // 自定义课程预约邮件
                subject = "【健身房】课程预约成功 - " + apply.getTitle();
                content = String.format(
                    "亲爱的%s，\n\n" +
                    "您好！您的课程预约已审核通过。\n\n" +
                    "预约信息如下：\n" +
                    "课程标题：%s\n" +
                    "课程内容：%s\n" +
                    "预约时间：%s\n\n" +
                    "请您准时参加课程，如有任何问题请及时联系我们。\n\n" +
                    "祝您健身愉快！\n" +
                    "健身房管理团队",
                    user.getName(),
                    apply.getTitle(),
                    apply.getContent(),
                    apply.getTime()
                );
            }
            
            System.out.println("邮件主题: " + subject);
            System.out.println("收件人: " + user.getEmail());
            
            // 立即发送邮件到用户邮箱
            com.example.entity.Mail mail = new com.example.entity.Mail();
            mail.setFrom("19978018409@163.com");
            mail.setTo(user.getEmail());
            mail.setSubject(subject);
            mail.setContent(content);
            
            System.out.println("=== 开始发送邮件 ===");
            mailService.sendSimpleMailMessage(mail);
            System.out.println("✅ 预约成功邮件已发送到：" + user.getEmail());
            
            // 同时创建定时邮件任务（30秒后发送第2次提醒）
            System.out.println("=== 创建定时邮件任务 ===");
            scheduledMailService.createScheduledMail(
                user.getId(),
                null,
                user.getEmail(),
                "【第2次提醒】" + subject,
                "【最后一次提醒】\n\n" + content + "\n\n这是最后一次提醒，请务必准时参加！",
                0 // 立即创建任务，30秒后发送
            );
            System.out.println("✅ 定时邮件任务创建完成");
            System.out.println("=== 邮件提醒创建完成 ===");
            
        } catch (Exception e) {
            // 邮件发送失败不影响主流程
            System.err.println("❌ 发送预约成功邮件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteById(Integer id) {
        applyMapper.deleteById(id);
    }


    public List<Apply> selectAll(Apply apply){
        return applyMapper.selectAll(apply);
    }

    public PageInfo<Apply> selectPage(Integer pageNum, Integer pageSize,  Apply apply) {
        //查之前先给他一个条件
        Account currentUser = TokenUtils.getCurrentUser();
        if ("USER".equals(currentUser.getRole())){
            apply.setUserId(currentUser.getId());
        }
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> list = applyMapper.selectAll(apply);
        return PageInfo.of(list);
    }

}
