package com.entry.dto;

import com.entry.entity.mysql.Subject;
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

    public SuccessResult(String msg){
        this.msg=msg;
    }

    public SuccessResult(Object data, String msg){
        this.data=data;
        this.msg=msg;
    }
}
