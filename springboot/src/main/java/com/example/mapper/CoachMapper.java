package com.example.mapper;

import com.example.entity.Coach;
import com.example.entity.Record;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CoachMapper {
    List<Coach> selectAll(Coach coach);

    void insert(Coach coach);

    void updateById(Coach coach);
    
    void deleteById(Integer id);
    
    Coach selectById(Integer id);
    
    /**
     * 根据用户名查询教练
     */
    Coach selectByUsername(String username);
}
