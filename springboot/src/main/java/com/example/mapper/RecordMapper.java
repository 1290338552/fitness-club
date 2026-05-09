package com.example.mapper;

import com.example.entity.Record;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RecordMapper {
    List<Record> selectAll(Record record);

    void insert(Record record);

    void updateById(Record record);
    @Delete("delete from record where id = #{id}")
    void deleteById(Integer id);

    @Select("SELECT r.*, u.name as userName, c.coach as coachName, c.img as coachImg " +
            "FROM record r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "LEFT JOIN coach c ON r.coach_id = c.id " +
            "WHERE r.id = #{id}")
    Record selectById(Integer id);
    
}
