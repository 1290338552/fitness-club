package com.example.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    UserService userService;

    @PostMapping("/add")
    public Result add(@RequestBody User user) {//@RequestBody接受前端传来的json参数
        userService.add(user);
            return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {//@RequestBody接受前端传来的json参数
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {//@PathVariable接受前端传来的路径参数
        userService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<User> list) {//@RequestBody接受前端传来的json参数
        userService.deleteBatch(list);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(User user) {
        List<User> userList  = userService.selectAll(user);
        return Result.success(userList);
    }
    @GetMapping("/getEmailByUsername")
    public Result getEmailByUsername(@RequestParam String username) {
        if (StrUtil.isBlank(username)) {
            return Result.error("操作人名称不能为空");
        }
        User user = userService.selectByUsername(username);
        if (user == null) {
            return Result.error("未查询到操作人【" + username + "】的信息");
        }
        if (StrUtil.isBlank(user.getEmail())) {
            return Result.error("操作人【" + username + "】未绑定邮箱");
        }
        return Result.success(user.getEmail());
    }



    /**
     * 分页查询
     * @param pageNum:当前页码
     * @param pageSize:每页个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                              User user) {
        PageInfo<User> pageInfo = userService.selectPage(pageNum,pageSize,user);
        return Result.success(pageInfo);
    }

    /**
     * 导出数据
     * ids:1,2,3
     */
    @GetMapping("/export")
    public void exportData(User user, HttpServletResponse response) throws Exception {
        String ids = user.getIds();
        if (StrUtil.isNotBlank(ids)){
            String[] idsArr = ids.split(",");
            user.setIdsArr(idsArr);
        }
        //拿到所有数据
        List<User> list = userService.selectAll(user);
        //构建Writer对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //设置中文表头
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("phone", "手机号");
        writer.addHeaderAlias("email", "邮箱");
        //默认的，未添加的属性也会被写出
        writer.setOnlyAlias(true);
        //写出数据到writer
        writer.write(list, true);
        //设置输出的文件的名称以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String  fileName = URLEncoder.encode("管理员数据.xlsx", StandardCharsets.UTF_8) ;
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName +  ".xlsx");
        //写出到输出流，并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }
    /**
     * 导入数据
     */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
        //拿到输入流 构建reader对象
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //通过构建reader对象读取数据
        reader.addHeaderAlias("账号","username" );
        reader.addHeaderAlias("名称","name");
        reader.addHeaderAlias("手机号","phone");
        reader.addHeaderAlias("邮箱","email" );
        List<User> list = reader.readAll(User .class);
        //将数据插入数据库
        for (User user : list){
            userService.add(user);
        }
        return Result.success();
    }
}
