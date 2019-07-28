package com.entry.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.Subject;
import com.entry.entity.neo4j.Category;
import com.entry.entity.neo4j.Entry;
import com.entry.dto.BaseResultFactory;
import com.entry.repository.mysql.SubjectRepository;
import com.entry.repository.neo4j.CategoryRepository;
import com.entry.repository.neo4j.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
public class EntryController {

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    CategoryRepository categoryRepository;

//    @PostMapping("/api/entry/setAssignments")
//    public ResponseEntity<?> setAssignment(@RequestBody String jsonParam){
//        try{
//            JSONObject json = JSONObject.parseObject(jsonParam);
//            Integer subjectId = json.getInteger("subjectId");
//            List<JSONObject> assignmentList = JSONObject.parseArray(jsonParam.get)
//        }catch (IOException e){
//            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/api/entry/createAssignment")
    public ResponseEntity<?> setAssignment(@RequestBody String jsonParam){
        try{
            JSONObject json = JSONObject.parseObject(jsonParam);
            Integer subjectId = json.getInteger("subjectId");
            Subject subject = subjectRepository.findSubjectById(subjectId);
            JSONArray nodes = json.getJSONArray("nodes");
            JSONArray edges = json.getJSONArray("edges");
            for(int i=0;i<nodes.size();i++){
                JSONObject  node = nodes.getJSONObject(i);
                String entryName = node.getString("entryName");
                Assignment assignment = new Assignment(entryName,subject.getField(),subject);

            }
            return new ResponseEntity<>(BaseResultFactory.build("success"),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }
}
