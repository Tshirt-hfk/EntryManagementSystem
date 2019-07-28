package com.entry.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.entity.mysql.*;
import com.entry.exception.MyException;
import com.entry.repository.mysql.*;
import com.entry.dto.BaseResultFactory;
import com.entry.service.impl.SubjectManagementServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    SubjectManagementServiceImpl subjectManagementServiceImpl;
    /**
     * 用户登陆状态
     * @param request
     * @return
     */
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

    /**
     * 获取该用户的所有词条任务task
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/user/getEntry")
    @CrossOrigin
    public ResponseEntity<?> getEntry(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer type = (Integer)form.get("type");
            List<Task> tasks = taskRepository.findAllByUser_IdAndState(userId,type);
            List<Object> list = new ArrayList<>();
            HashMap<String, Object> tmp = null;
            for(Task task: tasks){
                tmp = new HashMap<>();
                tmp.put("id", task.getId());
                tmp.put("content", task.getContent());
                tmp.put("endTime", task.getDeadline());
                tmp.put("saveTime", task.getSaveTime());
             //   tmp.put("judgeTime", task.getJudgeTime());
                tmp.put("reason", task.getAdmitReason());
                tmp.put("name", task.getEntryName());
                tmp.put("field", task.getField());
                list.add(tmp);
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("assignments", list);
            return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取词条task内容
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/user/getTaskContent")
    @CrossOrigin
    public ResponseEntity<?> getTaskContent(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer entryId = form.getInteger("taskId");
            Task task = taskRepository.findTaskById(entryId);
            if(task!=null && task.getUser().getId()==userId){
                HashMap<String,Object> result = new HashMap<>();
                result.put("entryName",task.getEntryName());
                result.put("imageUrl",task.getImageUrl());
                result.put("intro",task.getIntro());
                result.put("field",task.getField());
                result.put("infoBox", task.getInfoBox());
                result.put("content", task.getContent());
                return new ResponseEntity<>(BaseResultFactory.build(result, "success"), HttpStatus.OK);
            }else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户没有词条"), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 暂存词条task
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/user/saveTaskContent")
    @CrossOrigin
    public ResponseEntity<?> saveTaskContent(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject data = JSONObject.parseObject(jsonParam);
            Integer taskId = data.getInteger("taskId");
            JSONObject form = data.getJSONObject("form");
            String entryName = form.getString("entryName");
            String imageUrl = form.getString("imageUrl");
            JSONArray field = form.getJSONArray("field");
            String intro = form.getString("intro");
            JSONArray infoBox = form.getJSONArray("infoBox");
            String content = form.getString("content");
            JSONArray reference = form.getJSONArray("reference");
            Task task = taskRepository.findTaskById(taskId);
            subjectManagementServiceImpl.saveTask(userId, taskId, entryName, imageUrl, field, intro, infoBox, content, reference);
            return new ResponseEntity<>(BaseResultFactory.build("编辑成功"), HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),me.getMessage()),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"编辑错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户提交词条task
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/user/admitEntry")
    @CrossOrigin
    public ResponseEntity<?> admitEntry(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            JSONArray entryIds = form.getJSONArray("entryIds");
            String reason = form.getString("reason");
            int len = entryIds.size();
            for (int i=0;i<len;i++){
                Integer taskId = entryIds.getInteger(i);
                subjectManagementServiceImpl.submitTask(userId, taskId, reason);
            }
            return new ResponseEntity<>(BaseResultFactory.build("提交成功"), HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),me.getMessage()),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"提交错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户放弃任务
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/user/giveUpTask")
    @CrossOrigin
    @Transactional
    public ResponseEntity<?> giveUpTask(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            List<Integer> entryIds = (List<Integer>) form.get("entryIds");
            for (Integer id : entryIds){
                Task task = taskRepository.findTaskById(id);
                Assignment assignment = task.getAssignment();
                assignment.setState(Assignment.PUBLISHED);
                assignmentRepository.save(assignment);
                taskRepository.delete(task);
            }
            return new ResponseEntity<>(BaseResultFactory.build("删除成功"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"删除错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 获取专题基本信息
     * @param request
     * @param jsonParam
     * @return
     */
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
                result1.put("basicInfo",result);
            }else {
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(), "该专题不存在"), HttpStatus.NOT_FOUND);
            }
            if(groupMember!=null)
                result.put("myCompletedCount",groupMember.getMyCompletedCount());
            else
                result.put("myCompletedCount",0);
            return new ResponseEntity<>(BaseResultFactory.build(result1, "success"), HttpStatus.OK);
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
    public ResponseEntity<?> getAssignment(@RequestBody String jsonParam){
        try{
            System.out.println(jsonParam);
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer)form.get("subjectId");
            List<Assignment> assignments = assignmentRepository.findAllBySubject_IdAndState(subjectId,Assignment.PUBLISHED);
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
            GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(userId,subjectId);
            if(groupMember == null) {
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(), "未加入该专题"), HttpStatus.NOT_FOUND);
            }
            Integer type = (Integer)form.get("type");
            List<Task> tasks = taskRepository.findAllBySubject_IdAndUser_IdAndState(subjectId,userId,type);
            HashMap<String,Object> result1;
            List<Object> list = new ArrayList<>();
            for(Task task : tasks){
                result1=new HashMap<>();
                result1.put("id",task.getId());
                result1.put("name",task.getEntryName());
                result1.put("deadline",task.getDeadline());
                list.add(result1);
            }
            HashMap<String,Object> result=new HashMap<>();
            result.put("tasks",list);
            return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户领取任务
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/drawAssignment")
    @CrossOrigin
    public ResponseEntity<?> drawAssignment(HttpServletRequest request,@RequestBody String jsonParam){
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer assignmentId = (Integer)form.get("assignmentId");
            subjectManagementServiceImpl.drawAssignment(userId, assignmentId);
            return new ResponseEntity<>(BaseResultFactory.build("领取成功"), HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),me.getMessage()),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 用户获取参加的专题
     * @param request
     * @param
     * @return
     */
    @PostMapping("/api/user/getSubject")
    @CrossOrigin
    public ResponseEntity<?> getSubject(HttpServletRequest request){
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            List<GroupMember> groupMembers = groupMemberRepository.findAllByUser_IdAndIdentity(userId,GroupMember.ORDINRYUSER);
            if(groupMembers == null){
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),""),HttpStatus.BAD_REQUEST);
            }
            List<Object> tmps = new ArrayList<>();
            HashMap<String,Object> tmp = null;
            for(GroupMember groupMember : groupMembers){
                Subject subject = groupMember.getSubject();
                List<Task> tasks = taskRepository.findAllByUser_IdAndState(userId, Task.PASS);
                tmp = new HashMap<>();
                tmp.put("id", subject.getId());
                tmp.put("name", subject.getName());
                tmp.put("imgUrl", subject.getImageUrl());
                tmp.put("deadline", subject.getDeadline());
                tmp.put("finishNum", tasks.size());
                tmp.put("introduction", subject.getIntroduction());
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
     * 用户加入该专题
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/joinSubject")
    @CrossOrigin
    public ResponseEntity<?> joinSubject(HttpServletRequest request,@RequestBody String jsonParam){
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer subjectId = (Integer)form.get("subjectId");
            User user = userRepository.findUserById(userId);
            Subject subject = subjectRepository.findSubjectById(subjectId);
            GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(userId,subject.getId());
            if(groupMember == null){
                groupMember = new GroupMember(subject,user,GroupMember.ORDINRYUSER);
                groupMemberRepository.save(groupMember);
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"加入该专题成功"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("已加入该专题"), HttpStatus.OK);
            }
        }catch (IOException e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户搜索专题名
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/searchSubject")
    @CrossOrigin
    public ResponseEntity<?> searchSubject(HttpServletRequest request,@RequestBody String jsonParam){
        try{
            JSONObject form = JSONObject.parseObject(jsonParam);
            String subjectName = form.getString("keyword");
            List<Subject> subjects = subjectRepository.findSubjectByKey(subjectName);
            List<Object> tmps = new ArrayList<>();
            JSONObject tmp = null;
            if(subjects.size() != 0){
                for(Subject subject: subjects){
                    tmp = new JSONObject();
                    tmp.put("id", subject.getId());
                    tmp.put("name", subject.getName());
                    tmp.put("field", subject.getField());
                    tmp.put("deadTime", subject.getDeadline());
                    tmp.put("total_count", subject.getTotalCount());
                    tmps.add(tmp);
                }
                HashMap<String,Object> result = new HashMap<>();
                result.put("subjects", tmps);
                return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("无结果"), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户搜索词条名
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/searchEntry")
    @CrossOrigin
    public ResponseEntity<?> searchEntry(HttpServletRequest request,@RequestBody String jsonParam){
        try{
            JSONObject form = JSONObject.parseObject(jsonParam);
            String entryName = (String)form.get("keyword");
            List<Assignment> assignments = assignmentRepository.findAssignmentByKey(entryName);
            JSONArray tmps = new JSONArray();
           JSONObject tmp = null;
            String reason = null;
            String reasonResult [] = null;
            if(assignments.size() != 0){
                for(Assignment assignment: assignments){
                    tmp = new JSONObject();
                    tmp.put("id", assignment.getId());
                    tmp.put("name", assignment.getEntryName());
                    tmp.put("field", assignment.getField());
                    reason = assignment.getModifyReason();
                    if(reason != null) {
                        reasonResult = reason.split(";");
                        if(reasonResult.length == 2) {
                            tmp.put("reason1", reasonResult[0]);
                            tmp.put("reason2", reasonResult[1]);
                        }
                    }
                    tmps.add(tmp);
                }
                JSONObject result = new JSONObject();
                result.put("assignments", tmps);
                return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("无结果"), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户获得推荐专题
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/getRecommendSubject")
    @CrossOrigin
    public ResponseEntity<?> getRecommendSubject(HttpServletRequest request,@RequestBody String jsonParam){
            Integer userId = (Integer) request.getAttribute("userId");
            List<Subject> subjects = subjectRepository.findSubjectByRand(8);
            JSONArray tmps = new JSONArray();
            JSONObject tmp = null;
            if(subjects.size() != 0){
                for(Subject subject: subjects){
                    tmp = new JSONObject();
                    tmp.put("id", subject.getId());
                    tmp.put("name", subject.getName());
                    tmp.put("field", subject.getField());
                    tmp.put("deadTime", subject.getDeadline());
                    tmp.put("total_count", subject.getTotalCount());
                    tmp.put("imgUrl", subject.getImageUrl());
                    tmps.add(tmp);
                }
                JSONObject result = new JSONObject();
                result.put("subjects", tmps);
                return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("无结果"), HttpStatus.OK);
            }

    }

    /**
     * 用户获得推荐词条
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/getRecommendEntry")
    @CrossOrigin
    public ResponseEntity<?> getRecommendEntry(HttpServletRequest request,@RequestBody String jsonParam){
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            List<Assignment> assignments = assignmentRepository.getRecommendAssignmentByRand(12);
            List<Object> tmps = new ArrayList<>();
            HashMap<String,Object> tmp = null;
            String reason = null;
            String reasonResult [] = null;
            if(assignments.size() != 0){
                for(Assignment assignment: assignments){
                    tmp = new HashMap<>();
                    tmp.put("id", assignment.getId());
                    tmp.put("name", assignment.getEntryName());
                    tmp.put("field", assignment.getField());
                    reason = assignment.getModifyReason();
                    if(reason != null) {
                        reasonResult = reason.split(";");
                        if(reasonResult.length == 2) {
                            tmp.put("reason1", reasonResult[0]);
                            tmp.put("reason2", reasonResult[1]);
                        }
                    }
                    tmps.add(tmp);
                }
                HashMap<String,Object> result = new HashMap<>();
                result.put("assignments", tmps);
                return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("无结果"), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

}