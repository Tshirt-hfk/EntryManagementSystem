package com.entry.controller;

import com.entry.dto.BaseResultFactory;
import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.GroupMember;
import com.entry.entity.mysql.Subject;
import com.entry.entity.mysql.User;
import com.entry.entity.mysql.pk.GroupMemberPK;
import com.entry.repository.mysql.AssignmentRepository;
import com.entry.repository.mysql.GroupMemberRepository;
import com.entry.repository.mysql.SubjectRepository;
import com.entry.repository.mysql.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@RestController
public class SubjectMakerController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @PostMapping("/api/subjectmaker/subject")
    @CrossOrigin
    public ResponseEntity<?> postSubject(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            User user = userRepository.findUserById(userId);
            // 创建专题组
            System.out.println(jsonParam);
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            String name=(String)form.get("name");
            String field=(String)form.get("field");
            Boolean isPublic=(Boolean)form.get("isPublic");
            String introduction=(String)form.get("introduction");
            String goal=(String)form.get("goal");
            Subject subject = new Subject(name,introduction,goal,field,isPublic);
            // 将创建人加入专题组中
            GroupMember groupMember = new GroupMember(new GroupMemberPK(subject,user),2);
            subjectRepository.save(subject);
            groupMemberRepository.save(groupMember);
            return new ResponseEntity<>(BaseResultFactory.build("创建成功"), HttpStatus.OK);
        }catch (IOException e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/api/subjectmaker/subject")
    @CrossOrigin
    public ResponseEntity<?> getSubject(@RequestParam("id") Integer id){
        try{
            Subject subject = subjectRepository.findSubjectById(id);
            List<Assignment> assignments = subject.getAssignmentlist();
            HashMap<String,Object> result1=new HashMap<>();
            HashMap<String,Object> result2=new HashMap<>();
            HashMap<String,Object> result3=new HashMap<>();
            for(Assignment assignment : assignments){
                Integer state = assignment.getState();
                if(state==Assignment.UNPUBLISHED){
                    result1.put("name",assignment.getEntryName());
                    result1.put("id",assignment.getId());
                }else if(state==Assignment.PUBLISHED){
                    result2.put("name",assignment.getEntryName());
                    result2.put("id",assignment.getId());
                }else{
                    result3.put("name",assignment.getEntryName());
                    result3.put("id",assignment.getId());
                }
            }
            HashMap<String,Object> result=new HashMap<>();
            result.put("assignmentW",result1);
            result.put("assignmentD",result2);
            result.put("assignmentA",result2);
            return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

}
