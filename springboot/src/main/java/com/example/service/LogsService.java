package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Logs;
import com.example.entity.Notice;
import com.example.mapper.LogsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogsService {


    @Resource
    LogsMapper logsMapper;


    public void add(Logs logs) {
        logs.setTime(DateUtil.now());
        logsMapper.insert(logs);
    }


    public void deleteById(Integer id) {
        logsMapper.deleteById(id);
    }


    public List<Logs> selectAll(Logs logs){
        return logsMapper.selectAll(logs);
    }



    public void deleteBatch(List<Logs> list) {
        for (Logs logs : list) {
            this.deleteById(logs.getId());
        }
    }



    public PageInfo<Logs> selectPage(Integer pageNum, Integer pageSize,  Logs logs) {
        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Logs> list = logsMapper.selectAll(logs);
        return PageInfo.of(list);
    }




}
