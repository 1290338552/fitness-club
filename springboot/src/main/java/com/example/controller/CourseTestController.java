package com.example.controller;

import com.example.common.Result;
import com.example.entity.Course;
import com.example.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course/test")
public class CourseTestController {

    @Autowired
    private CourseService courseService;

    /**
     * 测试课程状态更新
     */
    @PutMapping("/updateStatus")
    public Result testUpdateStatus(@RequestParam Integer id, @RequestParam String status) {
        try {
            System.out.println("=== 测试课程状态更新 ===");
            System.out.println("课程ID: " + id);
            System.out.println("新状态: " + status);
            
            // 先查询课程信息
            Course course = courseService.selectById(id);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            System.out.println("课程名称: " + course.getName());
            System.out.println("当前状态: " + course.getStatus());
            
            // 更新状态
            courseService.updateStatus(id, status);
            
            // 再次查询验证
            Course updatedCourse = courseService.selectById(id);
            System.out.println("更新后状态: " + updatedCourse.getStatus());
            System.out.println("=== 状态更新完成 ===");
            
            return Result.success("状态更新成功: " + course.getName() + " -> " + status);
        } catch (Exception e) {
            System.err.println("状态更新失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 测试课程信息查询
     */
    @GetMapping("/info/{id}")
    public Result getCourseInfo(@PathVariable Integer id) {
        try {
            Course course = courseService.selectById(id);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            System.out.println("=== 课程信息 ===");
            System.out.println("ID: " + course.getId());
            System.out.println("名称: " + course.getName());
            System.out.println("状态: " + course.getStatus());
            System.out.println("图片: " + course.getImage());
            
            return Result.success(course);
        } catch (Exception e) {
            System.err.println("查询课程信息失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("查询失败: " + e.getMessage());
        }
    }
}