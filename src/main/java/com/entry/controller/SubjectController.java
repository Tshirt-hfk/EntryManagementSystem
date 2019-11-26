package com.entry.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.dto.BaseResultFactory;
import com.entry.entity.mysql.Subject;
import com.entry.entity.mysql.User;
import com.entry.repository.mysql.SubjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    @GetMapping("/api/subject/getSubjectCategory")
    @CrossOrigin
    public ResponseEntity<?> getSubjectCategory(){
        try{
            JSONArray categories = new JSONArray();
            categories.add("科学");
            categories.add("技术");
            return new ResponseEntity<>(BaseResultFactory.build(categories,"获取成功"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/subject/getRecommendSubjectByName")
    @CrossOrigin
    public ResponseEntity<?> getRecommendSubjectByName(@RequestBody String jsonParam){
        try{
            JSONObject data = JSONObject.parseObject(jsonParam);
            String Field = data.getString("field");
            JSONArray subjects = new JSONArray();
            List<Subject> subjectList = subjectRepository.findSubjectsByField(Field);
            for(Subject subject : subjectList) {
                JSONObject json = new JSONObject();
                json.put("id", subject.getId());
                json.put("name", subject.getName());
                json.put("imageUrl", subject.getImageUrl());
                json.put("intro", subject.getIntroduction());
                subjects.add(json);
            }
            return new ResponseEntity<>(BaseResultFactory.build(subjects,"获取成功"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }
}
