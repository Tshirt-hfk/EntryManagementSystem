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
import javax.transaction.Transactional;
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
    AssignmentRepository assignmentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

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
                HashMap<String, Object> restemp = null;
                for(Task task: tasks){
                    Assignment assignment = task.getAssignment();
                    Integer state = assignment.getState();
                    if(state == type) {
                        restemp = new HashMap<>();
                        restemp.put("id", assignment.getId());
                        restemp.put("name", assignment.getEntryName());
                        restemp.put("field", assignment.getField());
                        list.add(restemp);
                    }
                }
                System.out.println("list.size" + list.size());
                HashMap<String, Object> result = new HashMap<>();
                result.put("assignments", list);
                return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
            }else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户没有词条"), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("api/user/admitEntry")
    @CrossOrigin
    @Transactional
    public ResponseEntity<?> admitEntry(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            List<Integer> entryIds = (List<Integer>) form.get("entryIds");
            for (Integer id : entryIds){
                assignmentRepository.updateStateById(Assignment.TOAUDITED, id);
                taskRepository.updateStateByAssignmentId(Assignment.TOAUDITED, id);
            }
            return new ResponseEntity<>(BaseResultFactory.build("提交成功"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("api/user/deleteEntry")
    @CrossOrigin
    @Transactional
    public ResponseEntity<?> deleteEntry(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            List<Integer> entryIds = (List<Integer>) form.get("entryIds");
            for (Integer id : entryIds){
                assignmentRepository.deleteStateById(Assignment.TOAUDITED, id);
                taskRepository.deleteStateByAssignmentId(Assignment.TOAUDITED, id);
            }
            return new ResponseEntity<>(BaseResultFactory.build("提交成功"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("api/user/getSubject/basicInfo")
    @CrossOrigin
    public ResponseEntity<?> getSubjectBasicInfo(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer) form.get("subjectId");
            Subject subject = subjectRepository.findSubjectById(subjectId);
            GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(userId,subjectId);
            HashMap<String, Object> result = new HashMap<>();
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
                return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
            }else if(subject==null)
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"该专题不存在"), HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"未加入该专题"), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

}
