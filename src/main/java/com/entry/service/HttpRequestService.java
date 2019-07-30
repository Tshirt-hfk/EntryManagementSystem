package com.entry.service;

import com.alibaba.fastjson.JSONArray;
import com.entry.exception.MyException;

import java.util.List;

public interface HttpRequestService {

    public void requestInitSubject(Integer subjectId ,String subjectName, JSONArray field ,JSONArray documents, String intro, String goal) throws MyException;


}
