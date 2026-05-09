package com.example.common;


/**
 * 会员日志的操作类型枚举
 */
public enum LogType {
    ADD("新增"),UPDATE("修改"),DELETE("删除"),Batch_Delete("批量删除"),LOGIN("登录"),REGISTER("注册");

    private String value;

    public String getValue() {
        return value;
    }

    LogType(String value){
        this.value = value;
    }

}
