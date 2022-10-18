package com.yefeng.common;

//进行异常处理方法
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
