package com.example.service.aop;


import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.example.common.HoneyLogs;
import com.example.entity.Account;
import com.example.entity.Logs;
import com.example.entity.User;
import com.example.service.LogsService;
import com.example.utils.IpUtils;
import com.example.utils.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import cn.hutool.core.date.DateUtil;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
//标志成一个切面
@Aspect
@Slf4j
public class LogsAspect {
    @Resource
    LogsService logsService;
//
//    @AfterReturning(pointcut = "@annotation(honeyLogs)", returning = "jsonResult")
//    public void recordLog(JoinPoint joinPoint, HoneyLogs honeyLogs, Object jsonResult) {
////        log.info("返回结果为：{}", jsonResult);
//        //获取当前登录会员的信息
//        Account loginUser  = TokenUtils.getCurrentUser();
//        if (loginUser == null){//会员未登录的情况下，   loginUser是null   是null的话我们就要从参数里面获取操作人信息
//            //登录、注册
//            Object[] args = joinPoint.getArgs();
//            if (ArrayUtil.isNotEmpty( args)){
//                if (args[0] instanceof User){
//                    loginUser  = (User) args[0];
//                }
//            }
//        }
////         如果仍然没有获取到用户信息或者用户是管理员，则不记录日志
//        if (loginUser != null || "ADMIN".equals(loginUser.getRole())) {
//            return; // 直接返回，不记录ADMIN的操作日志
//        }
//
//        //获取HttpServletRequest
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = servletRequestAttributes.getRequest();
//        //获取ip信息
//        String ipAdder = IpUtils.getIpAdder(request);
//
//        //获取ip信息
//        //为了组装日志的实体对象
//        Logs logs = Logs.builder()
//                .user(loginUser.getUsername())
//                .operation(honeyLogs.operation())
//                .type(honeyLogs.type().getValue())
//                .ip(ipAdder)
//                // 在代码中替换为：
//                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .build();
//        //查数据到数据库
//        ThreadUtil.execAsync(() -> {
//            logsService.add(logs);
//        });
//
//    }


@AfterReturning(pointcut = "@annotation(honeyLogs)", returning = "jsonResult")
public void recordLog(JoinPoint joinPoint, HoneyLogs honeyLogs, Object jsonResult) {
    try {
        // 获取当前登录用户的信息
        Account loginUser = TokenUtils.getCurrentUser();
        if (loginUser == null) {
            // 会员未登录的情况下，loginUser是null，从参数里面获取操作人信息
            // 登录、注册
            Object[] args = joinPoint.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                if (args[0] instanceof Account) {
                    loginUser = (Account) args[0];
                }
            }
        }

        // 如果是管理员操作或无法获取用户信息，则不记录日志
        if (loginUser == null || "ADMIN".equals(loginUser.getRole())) {
            return;
        }

        // 获取IP信息
        String ipAdder = "unknown";
        try {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                ipAdder = IpUtils.getIpAdder(request);
            }
        } catch (Exception e) {
            // IP获取失败不影响日志记录
        }

        // 组装日志实体对象
        final Logs logs = Logs.builder()
                .user(loginUser.getUsername())
                .operation(honeyLogs.operation())
                .type(honeyLogs.type().getValue())
                .ip(ipAdder)
                .time(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        // 异步保存日志到数据库
        ThreadUtil.execAsync(() -> {
            try {
                logsService.add(logs);
            } catch (Exception e) {
                log.error("保存日志失败: {}", e.getMessage());
            }
        });
    } catch (Exception e) {
        // 捕获所有异常，避免影响主业务流程
        log.error("记录日志异常: {}", e.getMessage());
    }
}




}
