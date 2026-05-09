package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.*;
import com.example.exception.CustomerException;
import com.example.mapper.NoticeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Resource
    NoticeMapper noticeMapper;

    public void add(Notice notice) {
        //后台权限管理，只有管理员可以添加，前端已用v-if 判断，此处需要只是双层权限判断
//        Account currentUser = TokenUtils.getCurrentUser();
//        if ("USER".equals(currentUser.getRole())){
//            throw new CustomerException("500","权限不足");
//        }
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public void update(Notice notice) {
        //根据主键修改
        noticeMapper.updateById(notice);
    }

    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }


    public List<Notice> selectAll(Notice notice){
        return noticeMapper.selectAll(notice);
    }

    public PageInfo<Notice> selectPage(Integer pageNum, Integer pageSize,  Notice notice) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }





}
