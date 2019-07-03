package com.entry.dto;

public class BaseResultFactory {
    public static AbstractBaseResult build(String msg){
        return new SuccessResult(msg);
    }
    public static AbstractBaseResult build(Object self,String msg){
        return new SuccessResult(self,msg);
    }
    public static AbstractBaseResult build(int code,String msg,String detail){
        return new ErrorResult(code,msg,detail);
    }
    public static AbstractBaseResult build(int code,String msg){
        return new ErrorResult(code,msg);
    }
    public static AbstractBaseResult build(Object data){
        return new SuccessResult(data);
    }
}
