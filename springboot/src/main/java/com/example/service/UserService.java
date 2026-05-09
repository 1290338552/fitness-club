package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public void add(User user) {
        //根据新的账号查询数据库，是否存在同样的数据
        User dbuser = userMapper.selectByUsername(user.getUsername());
        if (dbuser != null){
            throw new RuntimeException("账号已存在");
        }
        //密码为空则设置默认密码
        if (StrUtil.isBlank(user.getPassword())){
            user.setPassword("123456");
        }
        if (StrUtil.isBlank(user.getName())){
            user.setName(user.getUsername());
        }
        user.setRole("USER");
        userMapper.insert(user);
    }

    public void update(User user) {
        //根据主键修改
        userMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<User> list) {
        for (User user : list) {
            this.deleteById(user.getId());
        }
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectAll(User user){
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize,  User user) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }


    public User login(Account account) {
        //验证账号
        User dbuUser =userMapper.selectByUsername(account.getUsername());
        if (dbuUser == null){
            throw new CustomerException("账号不存在");
        }
        //验证密码
        if (!dbuUser.getPassword().equals(account.getPassword())){
            throw new CustomerException("账户或密码错误");
        }
        //创建token并返回给前端
        String token = TokenUtils.createToken(dbuUser.getId() + "-" + "USER",dbuUser.getPassword());
        dbuUser.setToken(token);
        return dbuUser;
    }


    public void register(User user) {
        this.add(user);
    }


    public void updatePassword(Account account) {
        //判断一下用户输入的新密码何确认密码是否一之
        if (!account.getNewPassword().equals(account.getNew2Password())){
            throw new CustomerException("500","两次输入的密码不一致");
        }
        //校验一下原密码是否正确
        Account currentUser = TokenUtils.getCurrentUser();
        if (!account.getPassword().equals(currentUser.getPassword())){
            throw new CustomerException("500","原密码错误");
        }
        //开始更新密码
        User user = userMapper.selectById(currentUser.getId());
        user.setPassword(account.getNewPassword());
        userMapper.updateById(user);
    }

    // 根据用户名查询用户（含邮箱）
    public User selectByUsername(String username) {
        // 调用你Mapper中已有的selectByUsername方法
        return userMapper.selectByUsername(username);
    }

    public User selectByPhone(String phoneNumber) {
        // 直接调用 Mapper 的注解方法（无需通过接口转发）
        return userMapper.selectByPhone(phoneNumber);
    }





}
