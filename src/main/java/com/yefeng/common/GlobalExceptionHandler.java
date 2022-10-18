package com.yefeng.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
// 主要用精简客户端返回异常，它可以捕获各种异常
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    //@ExceptionHandler注解我们一般是用来自定义异常的。
    // 可以认为它是一个异常拦截器（处理器）。
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception) {
        log.info(exception.getMessage());
        return R.error(exception.getMessage());
    }
}
