package com.example.controller;

import com.example.common.Result;
import com.example.entity.Introduction;
import com.example.service.IntroductionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/introduction")
public class IntroductionController {


    @Resource
    IntroductionService introductionService;

    @PostMapping("/add")
    public Result add(@RequestBody Introduction introduction) {//@RequestBody接受前端传来的json参数
        introductionService.add(introduction);
            return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Introduction introduction) {//@RequestBody接受前端传来的json参数
        introductionService.update(introduction);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@PathVariable接受前端传来的路径参数
        introductionService.deleteById(id);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(Introduction introduction) {
        List<Introduction> introductionList  = introductionService.selectAll(introduction);
        return Result.success(introductionList);
    }


    /**
     * 分页查询
     * @param pageNum:当前页码
     * @param pageSize:每页个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                              Introduction introduction) {
        PageInfo<Introduction> pageInfo = introductionService.selectPage(pageNum,pageSize,introduction);
        return Result.success(pageInfo);
    }

    /**
     * 根据Id查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Introduction introduction = introductionService.selectById(id);
        return Result.success(introduction);
    }

}
