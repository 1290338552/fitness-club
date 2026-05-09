package com.example.mapper;

import com.example.entity.Logs;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

public interface LogsMapper {
    List<Logs> selectAll(Logs logs);
//
    void insert(Logs logs);
//
//    void updateById(Notice notice);
    @Delete("delete from logs where id = #{id}")
    void deleteById(Integer id);
    
}
