package com.entry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public abstract class AbstractBaseResult<T> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Links{

        private String self;
        private String next;
        private String last;

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getSelf() {
            return self;
        }

        public void setSelf(String self) {
            this.self = self;
        }
    }
    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
