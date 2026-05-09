package com.example.controller;

import com.example.common.Result;
import com.example.entity.Course;
import com.example.service.CourseService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 新增课程
     */
    @PostMapping("/add")
    public Result add(@RequestBody Course course) {
        courseService.add(course);
        return Result.success();
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        courseService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除课程
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            courseService.deleteById(id);
        }
        return Result.success();
    }

    /**
     * 修改课程
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Course course) {
        courseService.updateById(course);
        return Result.success();
    }

    /**
     * 根据ID查询课程
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Course course = courseService.selectById(id);
        return Result.success(course);
    }

    /**
     * 查询所有课程
     */
    @GetMapping("/selectAll")
    public Result selectAll(Course course) {
        List<Course> list = courseService.selectActiveCourses();
        return Result.success(list);
    }

    /**
     * 分页查询课程
     */
    @GetMapping("/selectPage")
    public Result selectPage(Course course,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Course> page = courseService.selectPage(course, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据教练ID查询课程
     */
    @GetMapping("/selectByCoachId/{coachId}")
    public Result selectByCoachId(@PathVariable Integer coachId) {
        List<Course> list = courseService.selectByCoachId(coachId);
        return Result.success(list);
    }

    /**
     * 根据分类ID查询课程
     */
    @GetMapping("/selectByCategoryId/{categoryId}")
    public Result selectByCategoryId(@PathVariable Integer categoryId) {
        List<Course> list = courseService.selectByCategoryId(categoryId);
        return Result.success(list);
    }

    /**
     * 启用/禁用课程
     */
    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestParam Integer id, @RequestParam String status) {
        courseService.updateStatus(id, status);
        return Result.success();
    }
}