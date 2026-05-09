package com.example.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件下载
 * 下载路径："http://localhost:9999/files/download/404.jpeg"
 */
@RestController
@RequestMapping("/files")
public class FileController {

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception{
        try {
            System.out.println("=== 文件上传开始 ===");
            System.out.println("文件名: " + file.getOriginalFilename());
            System.out.println("文件大小: " + file.getSize() + " bytes");
            System.out.println("文件类型: " + file.getContentType());
            
            if (file.isEmpty()) {
                System.err.println("错误: 上传文件为空");
                return Result.error("上传文件不能为空");
            }
            
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                System.err.println("错误: 文件类型不正确 - " + contentType);
                return Result.error("只能上传图片文件");
            }
            
            // 找到文件的位置
            String filePath = System.getProperty("user.dir") + "/files/";
            System.out.println("文件保存路径: " + filePath);
            
            //判断上传的路径是不是文件夹
            if (!FileUtil.isDirectory(filePath)){
                //创建文件夹
                FileUtil.mkdir(filePath);
                System.out.println("创建文件夹: " + filePath);
            }
            
            byte[] bytes = file.getBytes();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); //文件的原始名称
            String fullPath = filePath + fileName;
            
            //写入文件
            FileUtil.writeBytes(bytes, fullPath);
            String url = "http://localhost:9999/files/download/" + fileName;
            
            // 验证文件是否成功写入
            boolean fileExists = FileUtil.exist(fullPath);
            System.out.println("文件写入成功: " + fileName);
            System.out.println("文件存在验证: " + fileExists);
            System.out.println("生成的URL: " + url);
            System.out.println("=== 文件上传完成 ===");
            
            return Result.success(url);
        } catch (Exception e) {
            System.err.println("=== 文件上传失败 ===");
            System.err.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }


    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception{
        try {
            //找到文件的位置
            String filePath = System.getProperty("user.dir") + "/files/"; 
            String realPath = filePath + fileName;  
            
            System.out.println("请求下载文件：" + fileName);
            System.out.println("文件完整路径：" + realPath);
            
            boolean exist = FileUtil.exist(realPath);
            if (!exist){
                System.err.println("文件不存在：" + realPath);
                response.setStatus(404);
                return;
            }
            
            //读取文件的字节流
            byte[] bytes = FileUtil.readBytes(realPath);
            
            // 设置响应头
            String contentType = getContentType(fileName);
            response.setContentType(contentType);
            response.setHeader("Cache-Control", "max-age=3600");
            
            ServletOutputStream os = response.getOutputStream();
            //输出流对象把文件写出到客户端
            os.write(bytes);
            os.flush();
            os.close();
            
            System.out.println("文件下载成功：" + fileName);
            
        } catch (Exception e) {
            System.err.println("文件下载失败：" + fileName + ", 错误：" + e.getMessage());
            e.printStackTrace();
            response.setStatus(500);
        }
    }
    
    /**
     * 根据文件名获取Content-Type
     */
    private String getContentType(String fileName) {
        String extension = FileUtil.extName(fileName).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }

    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload( MultipartFile file) {
        String flag = System.currentTimeMillis() + "";
        String fileName = file.getOriginalFilename();
        try {
            // 找到文件的位置
            String filePath = System.getProperty("user.dir") + "/files/";
            //文件存储形式，时间戳-文件名
            FileUtil.writeBytes(file.getBytes(),filePath + flag + "-" + fileName);
            System.out.println(fileName + "上传成功");
            Thread.sleep(1L);
        }catch (Exception e){
            System.err.println(fileName + "上传失败");
        }
        String http = "http://localhost:9999/files/download/";
        Map<String, Object> resMap = new HashMap<>();
        //wangEditor上传图片成功返回的参数
        resMap.put("errno", 0);
        resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
        return resMap;
    }
    
    /**
     * 测试图片访问
     */
    @GetMapping("/test/{fileName}")
    public Result testImage(@PathVariable String fileName) {
        String filePath = System.getProperty("user.dir") + "/files/";
        String realPath = filePath + fileName;
        
        boolean exist = FileUtil.exist(realPath);
        if (exist) {
            String url = "http://localhost:9999/files/download/" + fileName;
            return Result.success("图片存在，访问地址：" + url);
        } else {
            return Result.error("图片不存在：" + realPath);
        }
    }
}
