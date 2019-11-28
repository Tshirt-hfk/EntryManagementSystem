package com.entry.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.Subject;
import com.entry.exception.MyException;
import com.entry.service.HttpRequestService;
import com.entry.util.HttpRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import com.entry.entity.mysql.Record;

import java.io.IOException;
import java.util.HashMap;

@Service("httpRequestService")
public class HttpRequestServiceImpl implements HttpRequestService {

    @Value("${dataServer.initSubject.url}")
    private String initSubjectUrl;

    @Value("${dataServer.submitted.url}")
    private String submittedUrl;


    @Override
    public String requestInitSubject(Integer subjectId , String subjectName, JSONArray field , JSONArray documents, String intro, String goal) throws MyException {
        JSONObject data = new JSONObject();
        data.put("subjectId", subjectId);
        data.put("topic_name",subjectName);
        data.put("need_domain", 0);
        data.put("description", intro);
        data.put("documents",documents);
//        System.out.println(data.toJSONString());
        String result = HttpRequestUtil.post(this.initSubjectUrl, data.toJSONString());
        return result;
    }

    @Override
        public String requestSubmitEntry(Assignment assignment) throws MyException {
            JSONObject data = new JSONObject();
            data.put("id", assignment.getOriginalId());
            data.put("imageUrl", assignment.getImageUrl());
            data.put("entryName",assignment.getEntryName());
            data.put("field", assignment.getField());
            data.put("intro", assignment.getIntro());
            data.put("infoBox",assignment.getInfoBox());
            data.put("content",assignment.getContent());
            data.put("relation",assignment.getRelation());
            String result = HttpRequestUtil.post(this.submittedUrl, data.toJSONString());
            return result;
    }

    @Override
    public String requestSubmitEntry(Record record) throws MyException {
        JSONObject data = new JSONObject();
        data.put("id", record.getOriginalId());
        data.put("imageUrl", record.getImageUrl());
        data.put("entryName",record.getEntryName());
        data.put("field", record.getField());
        data.put("intro", record.getIntro());
        data.put("infoBox",record.getInfoBox());
        data.put("content",record.getContent());
        data.put("relation",record.getRelation());
        String result = HttpRequestUtil.post(this.submittedUrl, data.toJSONString());
        return result;
    }

}
