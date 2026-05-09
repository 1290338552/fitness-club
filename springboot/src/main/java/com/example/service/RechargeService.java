package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Recharge;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.RechargeMapper;
import com.example.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RechargeService {

    @Resource
    RechargeMapper rechargeMapper;
    @Resource
    UserMapper userMapper;

    /** 充值 */
    @Transactional
    public void recharge(Integer userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomerException("充值金额必须大于0");
        }
        rechargeMapper.addBalance(userId, amount);
        Recharge record = new Recharge();
        record.setUserId(userId);
        record.setAmount(amount);
        record.setType("RECHARGE");
        record.setRemark("账户充值");
        rechargeMapper.insert(record);
    }

    /** 消费扣款（预约成功时调用），返回false表示余额不足 */
    @Transactional
    public boolean consume(Integer userId, BigDecimal amount, String remark) {
        int rows = rechargeMapper.deductBalance(userId, amount);
        if (rows == 0) return false;
        Recharge record = new Recharge();
        record.setUserId(userId);
        record.setAmount(amount);
        record.setType("CONSUME");
        record.setRemark(remark);
        rechargeMapper.insert(record);
        return true;
    }

    /** 查询当前用户余额 */
    public BigDecimal getBalance(Integer userId) {
        User user = userMapper.selectById(userId);
        return user != null && user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;
    }

    /** 查询流水记录 */
    public List<Recharge> getRecords(Integer userId) {
        return rechargeMapper.selectByUserId(userId);
    }
}
