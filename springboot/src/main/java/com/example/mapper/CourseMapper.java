package com.example.mapper;

import com.example.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    
    /**
     * 新增课程
     */
    int insert(Course course);
    
    /**
     * 根据ID删除课程
     */
    int deleteById(Integer id);
    
    /**
     * 更新课程信息
     */
    int updateById(Course course);
    
    /**
     * 根据ID查询课程
     */
    Course selectById(Integer id);
    
    /**
     * 分页查询课程列表
     */
    List<Course> selectPage(@Param("name") String name, 
                           @Param("status") String status,
                           @Param("categoryId") Integer categoryId);
    
    /**
     * 查询所有启用的课程
     */
    List<Course> selectActiveCourses();
    
    /**
     * 根据教练ID查询课程
     */
    List<Course> selectByCoachId(Integer coachId);
    
    /**
     * 根据分类ID查询课程
     */
    List<Course> selectByCategoryId(Integer categoryId);
    
    /**
     * 更新课程状态
     */
    int updateStatus(@Param("id") Integer id, @Param("status") String status);
}