package com.example.controller;

import com.example.common.Result;
import com.example.entity.Recharge;
import com.example.service.RechargeService;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recharge")
public class RechargeController {

    @Resource
    RechargeService rechargeService;

    /** 充值 */
    @PostMapping("/add")
    public Result recharge(@RequestBody Map<String, Object> body) {
        Integer userId = TokenUtils.getCurrentUser().getId();
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        rechargeService.recharge(userId, amount);
        return Result.success();
    }

    /** 查询余额 */
    @GetMapping("/balance")
    public Result getBalance() {
        Integer userId = TokenUtils.getCurrentUser().getId();
        return Result.success(rechargeService.getBalance(userId));
    }

    /** 查询流水 */
    @GetMapping("/records")
    public Result getRecords() {
        Integer userId = TokenUtils.getCurrentUser().getId();
        List<Recharge> list = rechargeService.getRecords(userId);
        return Result.success(list);
    }
}
