package com.entry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResult extends AbstractBaseResult {

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public SuccessResult(Object data){
        this.msg="success!";
        this.data=data;
    }

    public SuccessResult(String self){
        this.msg=self;
    }
}
