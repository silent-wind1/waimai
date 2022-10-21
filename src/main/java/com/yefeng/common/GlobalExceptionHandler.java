package com.yefeng.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
// 主要用精简客户端返回异常，它可以捕获各种异常
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    //@ExceptionHandler注解我们一般是用来自定义异常的。
    // TDDO: SASASAS
    // 可以认为它是一个异常拦截器（处理器）。

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());
        if (exception.getMessage().contains("Duplicate entry")) {
            String[] split = exception.getMessage().split(" ");
            String message = split[2] + "账号已存在";
            return R.error(message);
        }


        return R.error("未知错误");
    }
}
