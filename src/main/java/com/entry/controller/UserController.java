package com.entry.controller;

import com.entry.entity.mysql.*;
import com.entry.repository.mysql.*;
import com.entry.dto.BaseResultFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @GetMapping("/api/user/islogin")
    @CrossOrigin
    public ResponseEntity<?> isLogin(HttpServletRequest request){
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            User user = userRepository.findUserById(userId);
            if (user != null) {
                HashMap<String,String> result=new HashMap<>();
                result.put("name", user.getName());
                result.put("status", user.getAuthorith().toString());
                return new ResponseEntity<>(BaseResultFactory.build(result), HttpStatus.OK);
            }else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户未注册"), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("api/user/getEntry")
    @CrossOrigin
    public ResponseEntity<?> getEntry(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer type = (Integer)form.get("type");
            System.out.println("userId" + userId);
            System.out.println("type" + type);
            User user = userRepository.findUserById(userId);
            List<Task> tasks = user.getTaskList();
            System.out.println("tasks.size" + tasks.size());
            if(tasks.size() != 0){
                List<Object> list = new ArrayList<>();
                HashMap<String, Object> restemp = new HashMap<>();
                for(Task task: tasks){
                    Assignment assignment = task.getAssignment();
                    Integer state = assignment.getState();
                    if(state == type) {
                        restemp.put("name", assignment.getEntryName());
                    }
                }
                if(restemp.size() != 0)
                    list.add(restemp);
                System.out.println("restemp.size" + restemp.get("name"));
                HashMap<String, Object> result = new HashMap<>();
                result.put("assignments", list);
                return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
            }else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户没有词条"), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("api/user/getSubjectBasicInfo")
    @CrossOrigin
    public ResponseEntity<?> getSubjectBasicInfo(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer) form.get("subjectId");
            Subject subject = subjectRepository.findSubjectById(subjectId);
            GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(userId,subjectId);
            HashMap<String, Object> result = new HashMap<>();
            HashMap<String, Object> result1 = new HashMap<>();
            if(subject!=null && groupMember!=null){
                result.put("title", subject.getName());
                result.put("creator", subject.getCreator());
                result.put("isPublic", subject.getPublic());
                result.put("myCompletedCount",groupMember.getMyCompletedCount());
                result.put("currentCount",subject.getCurrentCount());
                result.put("totalCount",subject.getTotalCount());
                result.put("deadline", subject.getDeadline());
                result.put("introduction",subject.getIntroduction());
                result.put("goal",subject.getGoal());
                result1.put("basicInfo",result);
                return new ResponseEntity<>(BaseResultFactory.build(result1, "success"), HttpStatus.OK);
            }else if(subject==null)
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"该专题不存在"), HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"未加入该专题"), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 获取该专题下的Assignment
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/getAssignment")
    @CrossOrigin
    public ResponseEntity<?> getSubject(@RequestBody String jsonParam){
        try{
            System.out.println(jsonParam);
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer)form.get("subjectId");
            List<Assignment> assignments = assignmentRepository.findAllBySubjectAndState(subjectId,2);
            HashMap<String,Object> result1;
            List<Object> list = new ArrayList<>();
            for(Assignment assignment : assignments){
                result1=new HashMap<>();
                result1.put("id",assignment.getId());
                result1.put("name",assignment.getEntryName());
                list.add(result1);
            }
            HashMap<String,Object> result=new HashMap<>();
            result.put("assignments",list);
            return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取该专题下的Task
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/getTask")
    @CrossOrigin
    public ResponseEntity<?> getTask(HttpServletRequest request,@RequestBody String jsonParam){
        try{
            System.out.println(jsonParam);
            Integer userId = (Integer)request.getAttribute("userId");
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer)form.get("subjectId");
            Integer type = (Integer)form.get("type");
            List<Task> tasks = taskRepository.findAllBySubject_IdAndUser_IdAAndState(subjectId,userId,type);
            HashMap<String,Object> result1;
            List<Object> list = new ArrayList<>();
            for(Task task : tasks){
                result1=new HashMap<>();
                result1.put("id",task.getAssignment().getId());
                result1.put("name",task.getEntryName());
                result1.put("deadline",task.getDeadline());
                list.add(result1);
            }
            HashMap<String,Object> result=new HashMap<>();
            result.put("assignments",list);
            return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }


}
