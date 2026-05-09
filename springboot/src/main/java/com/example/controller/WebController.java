package com.example.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.HoneyLogs;
import com.example.common.LogType;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Coach;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.CoachService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class WebController {
    @Resource
    AdminService adminService;
    @Resource
    UserService  userService;
    @Resource
    CoachService coachService;

    //表示这是一个get请求的接口
    @GetMapping("/")//接口的路径，全局唯一的
    public Result hello(){
        return Result.success("sss");
    }

    @HoneyLogs(operation = "登录", type = LogType.LOGIN)
    @PostMapping("/login")
    public Result login(@RequestBody Account account ){
        Account dbAccount = null;
        if ("ADMIN".equals(account.getRole())){
            dbAccount = adminService.login(account);
        }else if ("USER".equals(account.getRole())){
            dbAccount = userService.login(account);
        }else if ("COACH".equals(account.getRole())){
            Coach coach = coachService.selectByUsername(account.getUsername());
            if (coach == null || !account.getPassword().equals(coach.getPassword())) {
                throw new CustomerException("用户名或密码错误");
            }
            dbAccount = coach;
        }else {
            throw new CustomerException("角色不存在");
        }

        if (dbAccount != null) {
            // 生成JWT token
            String token = JWT.create()
                    .withAudience(dbAccount.getId() + "-" + dbAccount.getRole())  // 载荷: 用户ID-角色
                    .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24小时过期
                    .sign(Algorithm.HMAC256(dbAccount.getPassword()));  // 使用密码作为密钥

            // 将token设置到返回对象中
            dbAccount.setToken(token);
        }

        return Result.success(dbAccount);
    }
    @HoneyLogs(operation = "注册", type = LogType.REGISTER)
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        userService.register(user);
        return Result.success();
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){

        if ("ADMIN".equals(account.getRole())){
            adminService.updatePassword(account);
        }
        if ("USER".equals(account.getRole())){
            userService.updatePassword(account);
        }
        if ("COACH".equals(account.getRole())){
            // 教练修改密码逻辑
            Coach coach = coachService.selectById(account.getId());
            if (coach != null) {
                coach.setPassword(account.getPassword());
                coachService.updateById(coach);
            }
        }

        return Result.success();
    }

    @PostMapping("/phone")
    public Result phoneLogin(@RequestBody Map<String, String> request) {
        String phone = request.get("phoneNumber");
        String role = request.get("role");

        Account account = null;
        if ("ADMIN".equals(role)) {
            account = adminService.selectByPhone(phone);
        } else if ("USER".equals(role)) {
            account = userService.selectByPhone(phone);
        } else if ("COACH".equals(role)) {
            // 教练手机号登录（需要在CoachUserMapper中添加selectByPhone方法）
            // 暂时返回错误
            return Result.error("教练角色暂不支持手机号登录");
        }

        if (account == null) {
            return Result.error("没有查询到该手机用户");
        }

        // 生成JWT token
        String token = JWT.create()
                .withAudience(account.getId() + "-" + account.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256(account.getPassword()));

        // 将token设置到返回对象中
        account.setToken(token);

        return Result.success(account);
    }




}
