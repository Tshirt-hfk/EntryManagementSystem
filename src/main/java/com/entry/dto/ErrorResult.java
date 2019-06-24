package com.entry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResult extends AbstractBaseResult {
    private Integer errcode;
    private String detail;

    public ErrorResult(int code, String title, String detail) {
        this.errcode = code;
        this.msg=title;
        this.detail = detail;
    }
    public ErrorResult(int code, String title) {
        this.errcode = code;
        this.msg=title;
    }

    public Integer getErrCode() {
        return errcode;
    }

    public void setErrCode(Integer code) {
        this.errcode = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
