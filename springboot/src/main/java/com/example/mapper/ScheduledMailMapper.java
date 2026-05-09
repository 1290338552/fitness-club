package com.example.mapper;

import com.example.entity.ScheduledMail;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduledMailMapper {
    
    @Insert("INSERT INTO scheduled_mail (user_id, record_id, to_email, subject, content, scheduled_time, status, sent_count, max_send_count, next_send_time, create_time) " +
            "VALUES (#{userId}, #{recordId}, #{toEmail}, #{subject}, #{content}, #{scheduledTime}, #{status}, #{sentCount}, #{maxSendCount}, #{nextSendTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ScheduledMail scheduledMail);
    
    @Select("SELECT * FROM scheduled_mail WHERE status = 'PENDING' AND next_send_time <= #{now}")
    List<ScheduledMail> selectPendingMails(LocalDateTime now);
    
    @Update("UPDATE scheduled_mail SET status = #{status}, sent_count = #{sentCount}, last_sent_time = #{lastSentTime}, " +
            "next_send_time = #{nextSendTime}, error_message = #{errorMessage} WHERE id = #{id}")
    int updateAfterSend(@Param("id") Integer id, @Param("status") String status, @Param("sentCount") Integer sentCount,
                       @Param("lastSentTime") LocalDateTime lastSentTime, @Param("nextSendTime") LocalDateTime nextSendTime,
                       @Param("errorMessage") String errorMessage);
    
    @Select("SELECT * FROM scheduled_mail WHERE record_id = #{recordId}")
    List<ScheduledMail> selectByRecordId(Integer recordId);
}