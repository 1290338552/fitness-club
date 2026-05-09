package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Coach;
import com.example.entity.Record;
import com.example.entity.User;
import com.example.mapper.CoachMapper;
import com.example.mapper.RecordMapper;
import com.example.mapper.UserMapper;
import com.example.service.RechargeService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Resource
    RecordMapper recordMapper;
    @Resource
    CoachMapper coachMapper;
    @Resource
    ScheduledMailService scheduledMailService;
    @Resource
    MailService mailService;
    @Resource
    UserMapper userMapper;

    @Resource
    RechargeService rechargeService;

    public void add(Record record) {
        record.setStatus("预约中");
        record.setTime(DateUtil.now());
        recordMapper.insert(record);

        //教练减一
            Coach coach = coachMapper.selectById(record.getCoachId());
            if (ObjectUtil.isNotEmpty( coach)){
                coach.setNum(coach.getNum() - 1);
                coachMapper.updateById(coach);
            }
    }

    public void update(Record record) {
        System.out.println("Record状态更新为：" + record.getStatus());

        // 审核通过时扣款
        if ("预约成功".equals(record.getStatus())) {
            Record old = recordMapper.selectById(record.getId());
            if (old != null && !"预约成功".equals(old.getStatus())) {
                Coach coach = coachMapper.selectById(record.getCoachId());
                if (coach != null && coach.getPrice() != null && !coach.getPrice().isEmpty()) {
                    try {
                        java.math.BigDecimal price = new java.math.BigDecimal(coach.getPrice());
                        boolean ok = rechargeService.consume(old.getUserId(), price, "预约教练：" + coach.getCoach());
                        if (!ok) {
                            throw new com.example.exception.CustomerException("会员余额不足，无法完成预约");
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        
        //根据主键修改
        recordMapper.updateById(record);
        Account currentUser = TokenUtils.getCurrentUser();
        if ("ADMIN".equals(currentUser.getRole()) && "预约失败".equals(record.getStatus())){
            //教练显示1
            Coach coach = coachMapper.selectById(record.getCoachId());
            if (ObjectUtil.isNotEmpty( coach)){
                coach.setNum(coach.getNum() + 1);
                coachMapper.updateById(coach);
            }
        }
        
        // 如果预约状态变为成功，立即发送邮件并创建提醒邮件任务
        if ("预约成功".equals(record.getStatus())) {
            System.out.println("检测到Record预约成功，开始发送邮件...");
            try {
                sendImmediateReminderMail(record.getId());
                System.out.println("已为预约记录 " + record.getId() + " 发送预约成功邮件");
            } catch (Exception e) {
                System.err.println("发送预约成功邮件失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void deleteById(Integer id) {
        recordMapper.deleteById(id);
    }


    public List<Record> selectAll(Record record){
        return recordMapper.selectAll(record);
    }

    public PageInfo<Record> selectPage(Integer pageNum, Integer pageSize,  Record record) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Record> list = recordMapper.selectAll(record);
        return PageInfo.of(list);
    }
    
    /**
     * 立即发送预约成功邮件
     */
    private void sendImmediateReminderMail(Integer recordId) {
        try {
            // 获取预约记录
            Record record = recordMapper.selectById(recordId);
            if (record == null || !"预约成功".equals(record.getStatus())) {
                return;
            }
            
            // 获取用户信息
            User user = userMapper.selectById(record.getUserId());
            if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
                System.out.println("用户邮箱为空，跳过邮件发送");
                return;
            }
            
            // 获取教练信息
            Coach coach = coachMapper.selectById(record.getCoachId());
            if (coach == null) {
                return;
            }
            
            // 构建邮件内容
            String subject = "【健身房】预约成功通知 - " + coach.getCoach() + "教练";
            String content = String.format(
                "亲爱的%s，\n\n" +
                "您好！您的教练预约已审核通过。\n\n" +
                "预约信息如下：\n" +
                "教练：%s（%s）\n" +
                "课程费用：￥%s\n" +
                "预约时间：%s\n\n" +
                "请您准时参加课程，如有任何问题请及时联系我们。\n\n" +
                "祝您健身愉快！\n" +
                "健身房管理团队",
                user.getName(),
                coach.getCoach(),
                coach.getName(),
                coach.getPrice(),
                record.getTime()
            );
            
            // 立即发送邮件
            com.example.entity.Mail mail = new com.example.entity.Mail();
            mail.setFrom("19978018409@163.com");
            mail.setTo(user.getEmail());
            mail.setSubject(subject);
            mail.setContent(content);
            
            mailService.sendSimpleMailMessage(mail);
            System.out.println("预约成功邮件已发送到：" + user.getEmail());
            
            // 同时创建定时邮件任务（30秒后发送第2次提醒）
            scheduledMailService.createScheduledMail(
                user.getId(),
                recordId,
                user.getEmail(),
                "【第2次提醒】" + subject,
                "【最后一次提醒】\n\n" + content + "\n\n这是最后一次提醒，请务必准时参加！",
                0 // 立即创建任务，30秒后发送
            );
            
        } catch (Exception e) {
            System.err.println("发送预约成功邮件失败: " + e.getMessage());
            e.printStackTrace();
        }
    }




}
