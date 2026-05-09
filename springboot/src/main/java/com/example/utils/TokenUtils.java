package com.example.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.example.service.CoachService;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Component
public class TokenUtils {
    @Resource
    AdminService adminService;
    @Resource
    UserService userService;
    @Resource
    CoachService coachService;

    static AdminService  staticAdminService;
    static UserService  staticUserService;
    static CoachService staticCoachService;

    //springboot启动后会加载这段代码
    @PostConstruct
    public void init(){
        staticAdminService = adminService;
        staticUserService = userService;
        staticCoachService = coachService;
    }
    /**
     * 生成token
     */
    public static String createToken(String data, String sign) {
        return JWT.create().withAudience(data) //将userID存入token中，作为载荷
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1)) //token的过期时间，这里设置为1天
                .sign(Algorithm.HMAC256(sign));//以password作为token密钥，HMAC256算法加密
    }

    /**
     * 获取当前登录用户信息
     */
//    public static Account getCurrentUser() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String token = request.getHeader("token");
//        if (StrUtil.isBlank(token)){
//            token  =  request.getParameter("token");
//        }
//
//
//        //拿到token的载荷数据
//        String audience = JWT.decode(token).getAudience().get(0);
//        String[] split = audience.split("-");
//        String  userId = split[0];
//        String role = split[1];
//        //根据token解析出来的userid去对应表查询用户
//        if ("ADMIN".equals(role)){
//            return staticAdminService.selectById(userId);
//        }else if ("USER".equals(role)){
//            return staticUserService.selectById(userId);
//        }
//        return null;
//    }

    /**
     * 获取当前登录用户信息
     */
    public static Account getCurrentUser() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return null;
            }

            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }

            // 添加token空值检查
            if (StrUtil.isBlank(token)) {
                return null;
            }

            // 拿到token的载荷数据
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            String userId = split[0];
            String role = split[1];
            // 根据token解析出来的userid去对应表查询用户
            if ("ADMIN".equals(role)) {
                return staticAdminService.selectById(userId);
            } else if ("USER".equals(role)) {
                return staticUserService.selectById(Integer.parseInt(userId));
            } else if ("COACH".equals(role)) {
                return staticCoachService.selectById(Integer.parseInt(userId));
            }
        } catch (Exception e) {
            // 捕获所有异常，包括JWTDecodeException
            return null;
        }
        return null;
    }


}
