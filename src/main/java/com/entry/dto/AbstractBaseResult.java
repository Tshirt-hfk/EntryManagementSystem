package com.entry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public abstract class AbstractBaseResult<T> {

    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
