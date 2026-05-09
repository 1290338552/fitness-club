package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {
    List<Admin> selectAll(Admin admin);

    void insert(Admin admin);

    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);

    void updateById(Admin admin);

    void deleteById(Integer id);

    @Select("select * from `admin` where id = #{id}")
    Admin selectById(String id);

    @Select("SELECT * FROM admin WHERE phone = #{phone} LIMIT 1")
    Admin selectByPhone(@Param("phone") String phone);
}
