package com.entry.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.dto.BaseResultFactory;
import com.entry.entity.mysql.*;
import com.entry.exception.MyException;
import com.entry.entity.mysql.pk.GroupMemberPK;
import com.entry.repository.mysql.*;
import com.entry.service.SubjectManagementService;
import com.entry.service.impl.SubjectManagementServiceImpl;
import com.entry.util.HttpRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SubjectManagementService subjectManagementService;

    /**
     * 获取创建的所有的专题的信息列表
     * @param request
     * @return
     */
    @PostMapping("/api/subjectMaker/getSubject")
    @CrossOrigin
    public ResponseEntity<?> getSubject(HttpServletRequest request){
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            List<GroupMember> groupMembers = groupMemberRepository.findAllByUser_IdAndIdentity(userId,GroupMember.SUBJECTMAKER);
            if(groupMembers==null){
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
            }
            List<Object> tmps = new ArrayList<>();
            HashMap<String,Object> tmp = null;
            for(GroupMember groupMember : groupMembers){
                Subject subject = groupMember.getSubject();
                List<Task> tasks = taskRepository.findAllBySubject_IdAndState(subject.getId(), Task.PASS);
                tmp = new HashMap<>();
                tmp.put("id", subject.getId());
                tmp.put("name", subject.getName());
                tmp.put("introduction", subject.getIntroduction());
                tmp.put("imageUrl", subject.getImageUrl());
                tmp.put("deadline", subject.getDeadline());
                tmp.put("finishNum", tasks.size());
                tmps.add(tmp);
            }
            HashMap<String,Object> result = new HashMap<>();
            result.put("subjects",tmps);
            return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }



    /**
     * 创建专题
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/subjectMaker/createSubject")
    @CrossOrigin
    public ResponseEntity<?> postSubject(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            String imageUrl = form.getString("imageUrl");
            String subjectName = form.getString("name");
            JSONArray field = form.getJSONArray("field");
            Boolean isPublic = form.getBoolean("isPublic");
            String introduction = form.getString("introduction");
            String goal = form.getString("goal");
            Long deadline = form.getLong("deadline");
            JSONArray documents = form.getJSONArray("documents");
            this.subjectManagementService.createSubject(userId,subjectName,imageUrl,field,isPublic,introduction,goal,deadline);
            //初始化 专题的所有任务

            return new ResponseEntity<>(BaseResultFactory.build("创建成功"), HttpStatus.OK);
        }catch (MyException me) {
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(), me.getMessage()), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 创建新的词条任务
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/subjectMaker/createEntry")
    @CrossOrigin
    public ResponseEntity<?> createEntry(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            String name = form.getString("name");
            JSONArray fields = form.getJSONArray("field");
            Integer subjectId = form.getInteger("subjectId");
            subjectManagementService.createAssignment(userId, subjectId, name, fields);
            return new ResponseEntity<>(BaseResultFactory.build("创建成功"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取该专题下的任务
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/subjectMaker/getAssignment")
    @CrossOrigin
    public ResponseEntity<?> getSubject(@RequestBody String jsonParam){
        try{
            System.out.println(jsonParam);
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer)form.get("subjectId");
            Integer type = (Integer)form.get("type");

            JSONObject result1;
            List<Object> list = new ArrayList<>();
            if(type >= Assignment.DRAWED){
                List<Task> tasks = taskRepository.findAllBySubject_IdAndState(subjectId,type);
                for(Task task : tasks) {
                    User user = task.getUser();
                    result1 = new JSONObject();
                    result1.put("id", task.getId());
                    result1.put("assignmentId", task.getAssignment().getId());
                    result1.put("name", task.getEntryName());
                    result1.put("field", task.getField());
                    result1.put("username", user.getName());
                    result1.put("userId", user.getId());
                    list.add(result1);
                }
            }else{
                List<Assignment> assignments = assignmentRepository.findAllBySubject_IdAndState(subjectId,type);
                for(Assignment assignment : assignments){
                    result1 = new JSONObject();
                    result1.put("id",assignment.getId());
                    result1.put("name",assignment.getEntryName());
                    result1.put("field",assignment.getField());
                    list.add(result1);
                }
            }
            HashMap<String,Object> result=new HashMap<>();
            result.put("assignments",list);
            return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取词条assignment内容
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/subjectMaker/getAssignmentContent")
    @CrossOrigin
    public ResponseEntity<?> getAssignmentContent(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer assignmentId = form.getInteger("assignmentId");
            Assignment assignment = assignmentRepository.findAssignmentById(assignmentId);
            Integer subjectId = assignment.getSubject().getId();
            Integer auth = groupMemberRepository.findByUser_IdAndSubject_Id(userId, subjectId).getIdentity();
            if(assignment != null && auth == GroupMember.SUBJECTMAKER){
                HashMap<String,Object> result = new HashMap<>();
                result.put("entryName",assignment.getEntryName());
                result.put("imageUrl",assignment.getImageUrl());
                result.put("intro",assignment.getIntro());
                result.put("field",assignment.getField());
                result.put("infoBox", assignment.getInfoBox());
                result.put("content", assignment.getContent());
                return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
            }else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户没有词条"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 专题制作人获取词条task内容
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/subjectMaker/getTaskContent")
    @CrossOrigin
    public ResponseEntity<?> getTaskContent(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer entryId = form.getInteger("taskId");
            Task task = taskRepository.findTaskById(entryId);
            Integer subjectId = task.getSubject().getId();
            Integer auth = groupMemberRepository.findByUser_IdAndSubject_Id(userId, subjectId).getIdentity();
            if(task != null && auth == GroupMember.SUBJECTMAKER){
                HashMap<String,Object> result = new HashMap<>();
                result.put("entryName",task.getEntryName());
                result.put("imageUrl",task.getImageUrl());
                result.put("intro",task.getIntro());
                result.put("field",task.getField());
                result.put("infoBox", task.getInfoBox());
                result.put("content", task.getContent());
                return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
            }else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户没有词条"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 发布任务
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/subjectMaker/publishAssignment")
    @CrossOrigin
    public ResponseEntity<?> publishAssignment(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            List<Integer> entryIds = (List<Integer>) form.get("entryIds");
            Integer subjectId = (Integer) form.get("subjectId");
            String reason = (String) form.get("reason");
            Integer time = (Integer) form.getOrDefault("dealine", 10*24*3600*1000);
            subjectManagementService.publishAssignment(userId, subjectId, reason, time, entryIds);
            return new ResponseEntity<>(BaseResultFactory.build("发布成功"), HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),me.getMessage()),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 审核
     */
    @PostMapping("/api/subjectMaker/auditTask")
    @CrossOrigin
    public ResponseEntity<?> auditTask(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Boolean pass = (Boolean)form.get("pass");
            Integer taskId = (Integer)form.get("taskId");
            String reason = (String) form.get("reason");
            subjectManagementService.auditTask(userId, taskId, pass, reason);
            if(pass){
                return new ResponseEntity<>(BaseResultFactory.build("审核通过"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("审核不通过"), HttpStatus.OK);
            }
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),me.getMessage()),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

}