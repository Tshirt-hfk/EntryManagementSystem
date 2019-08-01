package com.entry.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.dto.BaseResultFactory;
import com.entry.exception.MyException;
import com.entry.repository.mysql.SubjectRepository;
import com.entry.service.SubjectManagementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EntryController {

    private static Logger logger = LogManager.getLogger(EntryController.class);

    @Autowired
    SubjectRepository subjectRepository;


    @Autowired
    SubjectManagementService subjectManagementService;


    @PostMapping("/api/entry/initSubjectAssignment")
    public ResponseEntity<?> initSubjectAssignment(HttpServletRequest request, @RequestBody String jsonParam){
        logger.info("server response {}", request.getRequestURI());
        try{
            System.out.println(jsonParam);
            JSONObject json = JSONObject.parseObject(jsonParam);
            Integer subjectId = Integer.parseInt(json.getString("subjectId"));
            JSONArray nodes = json.getJSONArray("nodes");
            JSONArray edges = json.getJSONArray("edges");
            System.out.println(subjectId);
            System.out.println(nodes.toJSONString());
            System.out.println(edges.toJSONString());
            this.subjectManagementService.initSubjectAssignment(subjectId,nodes,edges);
            return new ResponseEntity<>(BaseResultFactory.build("success"),HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),me.getMessage()),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }
}
