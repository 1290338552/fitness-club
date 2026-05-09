package com.example.service;

import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 新增课程
     */
    public void add(Course course) {
        // 设置默认状态为启用
        if (course.getStatus() == null || course.getStatus().isEmpty()) {
            course.setStatus("ACTIVE");
        }
        courseMapper.insert(course);
    }

    /**
     * 删除课程
     */
    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }

    /**
     * 更新课程
     */
    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    /**
     * 根据ID查询课程
     */
    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }

    /**
     * 分页查询课程
     */
    public PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> list = courseMapper.selectPage(course.getName(), course.getStatus(), course.getCategoryId());
        return PageInfo.of(list);
    }

    /**
     * 查询所有启用的课程
     */
    public List<Course> selectActiveCourses() {
        return courseMapper.selectActiveCourses();
    }

    /**
     * 根据教练ID查询课程
     */
    public List<Course> selectByCoachId(Integer coachId) {
        return courseMapper.selectByCoachId(coachId);
    }

    /**
     * 根据分类ID查询课程
     */
    public List<Course> selectByCategoryId(Integer categoryId) {
        return courseMapper.selectByCategoryId(categoryId);
    }

    /**
     * 启用/禁用课程
     */
    public void updateStatus(Integer id, String status) {
        courseMapper.updateStatus(id, status);
    }
}