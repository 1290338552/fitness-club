package com.example.controller;

import com.example.common.Result;
import com.example.entity.Apply;
import com.example.entity.User;
import com.example.service.ApplyService;
import com.example.service.UserService;
import com.example.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug/email")
public class EmailDebugController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ApplyService applyService;

    /**
     * 检查当前登录用户的邮箱信息
     */
    @GetMapping("/current-user")
    public Result getCurrentUserEmail() {
        try {
            var currentUser = TokenUtils.getCurrentUser();
            System.out.println("=== 当前登录用户信息 ===");
            System.out.println("用户ID: " + currentUser.getId());
            System.out.println("用户名: " + currentUser.getUsername());
            System.out.println("角色: " + currentUser.getRole());
            
            // 获取完整用户信息
            User user = userService.selectById(currentUser.getId());
            if (user != null) {
                System.out.println("用户姓名: " + user.getName());
                System.out.println("用户邮箱: " + user.getEmail());
                System.out.println("用户手机: " + user.getPhone());
                
                return Result.success("当前用户: " + user.getName() + ", 邮箱: " + user.getEmail());
            } else {
                return Result.error("用户信息不存在");
            }
        } catch (Exception e) {
            System.err.println("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 检查指定用户的邮箱信息
     */
    @GetMapping("/user/{userId}")
    public Result getUserEmail(@PathVariable Integer userId) {
        try {
            User user = userService.selectById(userId);
            if (user != null) {
                System.out.println("=== 用户信息 ===");
                System.out.println("用户ID: " + user.getId());
                System.out.println("用户名: " + user.getUsername());
                System.out.println("用户姓名: " + user.getName());
                System.out.println("用户邮箱: " + user.getEmail());
                System.out.println("邮箱是否为空: " + (user.getEmail() == null || user.getEmail().isEmpty()));
                
                return Result.success(user);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            System.err.println("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 模拟预约状态更新，测试邮件发送
     */
    @PostMapping("/test-apply-update")
    public Result testApplyUpdate(@RequestParam Integer applyId) {
        try {
            System.out.println("=== 测试预约状态更新 ===");
            System.out.println("预约ID: " + applyId);
            
            // 模拟将状态改为预约成功
            Apply apply = new Apply();
            apply.setId(applyId);
            apply.setStatus("预约成功");
            
            applyService.update(apply);
            
            return Result.success("预约状态更新测试完成，请检查控制台日志和邮箱");
        } catch (Exception e) {
            System.err.println("测试预约更新失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("测试失败: " + e.getMessage());
        }
    }
}