package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Coach;
import com.example.mapper.CoachMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {

    @Resource
    CoachMapper coachMapper;

    public void add(Coach coach) {
        coachMapper.insert(coach);
    }

    public void update(Coach coach) {
        //根据主键修改
        coachMapper.updateById(coach);
    }

    public void deleteById(Integer id) {
        coachMapper.deleteById(id);
    }


    public List<Coach> selectAll(Coach coach){
        return coachMapper.selectAll(coach);
    }

    public PageInfo<Coach> selectPage(Integer pageNum, Integer pageSize,  Coach coach) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Coach> list = coachMapper.selectAll(coach);
        return PageInfo.of(list);
    }

    /**
     * 根据ID查询教练
     */
    public Coach selectById(Integer id) {
        return coachMapper.selectById(id);
    }

    /**
     * 根据用户名查询教练
     */
    public Coach selectByUsername(String username) {
        return coachMapper.selectByUsername(username);
    }

    /**
     * 更新教练信息
     */
    public void updateById(Coach coach) {
        coachMapper.updateById(coach);
    }
}
