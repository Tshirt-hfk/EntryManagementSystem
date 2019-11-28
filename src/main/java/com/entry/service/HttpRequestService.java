package com.entry.service;

import com.alibaba.fastjson.JSONArray;
import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.Record;
import com.entry.exception.MyException;

import java.util.List;

public interface HttpRequestService {

    public String requestInitSubject(Integer subjectId ,String subjectName, JSONArray field ,JSONArray documents, String intro, String goal) throws MyException;

    public String requestSubmitEntry(Assignment assignment) throws MyException;

    public String requestSubmitEntry(Record record) throws MyException;
}
