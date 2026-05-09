package com.example.controller;

import com.example.common.Result;
import com.example.entity.Coach;
import com.example.service.CoachService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coach")
public class CoachController {


    @Resource
    CoachService coachService;

    @PostMapping("/add")
    public Result add(@RequestBody Coach coach) {//@RequestBody接受前端传来的json参数
        coachService.add(coach);
            return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Coach coach) {//@RequestBody接受前端传来的json参数
        coachService.update(coach);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@PathVariable接受前端传来的路径参数
        coachService.deleteById(id);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(Coach coach) {
        List<Coach> coachList  = coachService.selectAll(coach);
        return Result.success(coachList);
    }


    /**
     * 分页查询
     * @param pageNum:当前页码
     * @param pageSize:每页个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                              Coach coach) {
        PageInfo<Coach> pageInfo = coachService.selectPage(pageNum,pageSize,coach);
        return Result.success(pageInfo);
    }

}
