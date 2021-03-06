package com.entry.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.dto.BaseResultFactory;
import com.entry.entity.mysql.*;
import com.entry.exception.MyException;
import com.entry.repository.mysql.*;
import com.entry.service.HttpRequestService;
import com.entry.service.SubjectManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SubjectManagementService subjectManagementService;

    @Autowired
    HttpRequestService httpRequestService;

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
                tmp = new HashMap<>();
                tmp.put("id", subject.getId());
                tmp.put("name", subject.getName());
                tmp.put("introduction", subject.getIntroduction());
                tmp.put("imageUrl", subject.getImageUrl());
                tmp.put("deadline", subject.getDeadline());
                tmp.put("finishNum", subject.getCurrentCount());
                tmp.put("initState",subject.getInitState());
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
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            String imageUrl = form.getString("imageUrl");
            String subjectName = form.getString("name");
            Subject subjectTest = subjectRepository.findSubjectByName(subjectName);
            if(subjectTest != null) {
                return new ResponseEntity<>(BaseResultFactory.build("该专题已存在"), HttpStatus.OK);
            }
            JSONArray field = form.getJSONArray("field");
            Boolean isPublic = form.getBoolean("isPublic");
            String introduction = form.getString("introduction");
            String goal = form.getString("goal");
            Long deadline = form.getLong("deadline");
            JSONArray documents = form.getJSONArray("documents");
            Subject subject = this.subjectManagementService.createSubject(userId, subjectName, imageUrl, field, isPublic, introduction, goal, deadline);
            String result = this.httpRequestService.requestInitSubject(subject.getId(), subjectName, field, documents, introduction, goal);
            JSONObject json = JSONObject.parseObject(result);
            if("success".equals(json.getString("status"))){
                JSONObject data = json.getJSONObject("data");
                subjectManagementService.initSubjectAssignment(subject.getId(),data.getJSONArray("nodes"),data.getJSONArray("edges"));
            }
            JSONObject res = new JSONObject();
            res.put("subjectId", subject.getId());
            return new ResponseEntity<>(BaseResultFactory.build(res,"创建成功"), HttpStatus.OK);
        } catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST,"创建请求"), HttpStatus.OK);
        }
    }

    /**
     * 查询专题信息
     * @param jsonParam
     * @return
     */
    @PostMapping("api/subjectMaker/getSubjectInfo")
    @CrossOrigin
    public ResponseEntity<?> getSubjectBasicInfo(@RequestBody String jsonParam) {
        try{
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer) form.get("subjectId");
            Subject subject = subjectRepository.findSubjectById(subjectId);
            HashMap<String, Object> result = new HashMap<>();
            if(subject!=null){
                result.put("imageUrl",subject.getImageUrl());
                result.put("title", subject.getName());
                result.put("creator", subject.getCreator());
                result.put("isPublic", subject.getPublic());
                result.put("currentCount",subject.getCurrentCount());
                result.put("totalCount",subject.getTotalCount());
                result.put("deadline", subject.getDeadline());
                result.put("introduction",subject.getIntroduction());
                result.put("goal",subject.getGoal());
            }else {
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(), "该专题不存在"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
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
//            System.out.println(jsonParam);
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer)form.get("subjectId");
            Subject subject = subjectRepository.findSubjectById(subjectId);
            Integer type = (Integer)form.get("type");
            JSONObject result1;
            List<Object> list = new ArrayList<>();
            if(type >= Assignment.DRAWED && type<Assignment.TOSUBMIT){
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
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer assignmentId = form.getInteger("assignmentId");
            JSONObject assignment = subjectManagementService.getAssignment(userId,assignmentId);
            return new ResponseEntity<>(BaseResultFactory.build(assignment, "success"), HttpStatus.OK);
        } catch (MyException me) {
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),me.getMessage()), HttpStatus.OK);
        } catch (Exception e){
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
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer taskId = form.getInteger("taskId");
            JSONObject task = this.subjectManagementService.getTask(userId,taskId);
            return new ResponseEntity<>(BaseResultFactory.build(task, "success"), HttpStatus.OK);
        } catch (MyException me) {
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),me.getMessage()), HttpStatus.OK);
        } catch (Exception e){
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
    public ResponseEntity<?> publishAssignment(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            List<Integer> entryIds = (List<Integer>) form.get("entryIds");
            Integer subjectId = (Integer) form.get("subjectId");
            JSONArray reasons = form.getJSONArray("reason");
            Integer time = (Integer) form.getOrDefault("deadline", 24*3600*1000);
            subjectManagementService.publishAssignment(userId, subjectId, reasons, time, entryIds);
            return new ResponseEntity<>(BaseResultFactory.build("发布成功"), HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),me.getMessage()),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 审核
     * @param request
     * @param jsonParam
     * @return
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

    /**
     * 制作人删除任务
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/subjectMaker/deleteAssignment")
    @CrossOrigin
    @Transactional
    public ResponseEntity<?> deleteAssignment(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer assignmentId = (Integer) form.get("assignmentId");
            Assignment assignment = assignmentRepository.findAssignmentById(assignmentId);
            GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(userId, assignment.getSubject().getId());
            if(groupMember.getIdentity() == GroupMember.SUBJECTMAKER)
                assignmentRepository.delete(assignment);
            else
                return new ResponseEntity<>(BaseResultFactory.build("没有权限"), HttpStatus.OK);
            return new ResponseEntity<>(BaseResultFactory.build("删除成功"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"删除错误"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("api/subjectMaker/submitEntry")
    @CrossOrigin
    public ResponseEntity<?> submitEntry(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            JSONArray entryIds = form.getJSONArray("entryIds");
            for(int i=0;i<entryIds.size();i++) {
                Integer assignmentId = entryIds.getInteger(i);
                Assignment assignment = this.subjectManagementService.submitEntry(userId, assignmentId);
                String result = this.httpRequestService.requestSubmitEntry(assignment);
            }
            return new ResponseEntity<>(BaseResultFactory.build("提交成功"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"删除错误"),HttpStatus.BAD_REQUEST);
        }
    }

}