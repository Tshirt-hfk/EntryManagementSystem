package com.entry.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.entity.mysql.Subject;
import com.entry.exception.MyException;
import com.entry.service.HttpRequestService;
import com.entry.util.HttpRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service("httpRequestService")
public class HttpRequestServiceImpl implements HttpRequestService {

    @Value("${dataServer.initSubject.url}")
    private String initSubjectUrl;

    @Override
    public void requestInitSubject(Integer subjectId , String subjectName, JSONArray field , JSONArray documents, String intro, String goal) throws MyException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("topic_name",subjectName);
        jsonObject.put("need_domain", 1);
        jsonObject.put("description", intro);
        jsonObject.put("documents",documents);
        String result = HttpRequestUtil.post(this.initSubjectUrl, jsonObject.toJSONString());
        System.out.println(result);
    }

}
