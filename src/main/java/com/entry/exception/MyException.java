package com.entry.exception;

import java.lang.Exception;

public class MyException extends Exception {

    private String msg;

    public MyException(String msg) {
        this.msg=msg;
    }

    @Override
    public void printStackTrace() {
        System.out.println("my exception");
    }

    @Override
    public String  getMessage() {
        return this.msg;
    }

}
