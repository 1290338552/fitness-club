package com.example.controller;

import com.example.common.Result;
import com.example.entity.Logs;
import com.example.service.LogsService;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogsController {


    @Resource
    LogsService logsService;
    @Resource
    UserService userService;


    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@PathVariable接受前端传来的路径参数
        logsService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Logs> list) {//@RequestBody接受前端传来的json参数
        logsService.deleteBatch(list);
        return Result.success();
    }


    /**
     * 分页查询
     * @param pageNum:当前页码
     * @param pageSize:每页个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                              Logs Logs) {
        PageInfo<Logs> pageInfo = logsService.selectPage(pageNum,pageSize,Logs);
        return Result.success(pageInfo);
    }

}
