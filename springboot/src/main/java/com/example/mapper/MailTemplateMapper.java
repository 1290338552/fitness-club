package com.example.mapper;

import com.example.entity.MailTemplate;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MailTemplateMapper {
    
    @Select("SELECT * FROM mail_template WHERE type = #{type}")
    MailTemplate selectByType(String type);
    
    @Insert("INSERT INTO mail_template (type, subject, content) VALUES (#{type}, #{subject}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MailTemplate mailTemplate);
    
    @Update("UPDATE mail_template SET subject = #{subject}, content = #{content} WHERE type = #{type}")
    int updateByType(MailTemplate mailTemplate);
}