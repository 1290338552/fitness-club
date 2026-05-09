package com.example.mapper;

import com.example.entity.Recharge;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

public interface RechargeMapper {
    void insert(Recharge recharge);
    List<Recharge> selectByUserId(Integer userId);

    @Update("UPDATE `user` SET balance = balance + #{amount} WHERE id = #{userId}")
    void addBalance(Integer userId, BigDecimal amount);

    @Update("UPDATE `user` SET balance = balance - #{amount} WHERE id = #{userId} AND balance >= #{amount}")
    int deductBalance(Integer userId, BigDecimal amount);
}
