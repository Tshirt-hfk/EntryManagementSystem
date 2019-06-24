package com.entry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResult extends AbstractBaseResult {
    private Links links;

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
        this.msg="success!";
        links=new Links();
        links.setSelf(self);
    }
    public SuccessResult(String self,int next,int last){
        this.msg="success!";
        links=new Links();
        links.setSelf(self);
        links.setNext(self+"?page="+next);
        links.setLast(self+"?page="+last);
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
