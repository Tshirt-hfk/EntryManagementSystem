package com.entry.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.entity.mysql.*;
import com.entry.exception.MyException;
import com.entry.repository.mysql.*;
import com.entry.dto.BaseResultFactory;
import com.entry.service.SubjectManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
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
    RecordRepository recordRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    SubjectManagementService subjectManagementService;

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
     * 获取该用户的所有词条任务task以及所有记录Record
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/user/getEntry")
    @CrossOrigin
    public ResponseEntity<?> getEntry(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer type = form.getInteger("type");
            //得到专题内任务词条
            List<Task> tasks = taskRepository.findAllByUser_IdAndState(userId,type);
            List<Object> list = new ArrayList<>();
            HashMap<String, Object> tmp = null;
            for(Task task: tasks) {
                Assignment assignment = task.getAssignment();
                tmp = new HashMap<>();
                tmp.put("id", task.getId());
                tmp.put("content", task.getContent());
                tmp.put("endTime", task.getDeadline());
                tmp.put("saveTime", task.getSaveTime());
                //   tmp.put("judgeTime", task.getJudgeTime());
                tmp.put("reason", task.getAdmitReason());
                tmp.put("modifyReason", assignment.getModifyReason());
                tmp.put("name", task.getEntryName());
                tmp.put("field", task.getField());
                tmp.put("source", 2);
                list.add(tmp);
            }
            //得到普通记录
            List<Record> records = recordRepository.findAllByUser_IdAndState(userId, type);
            for(Record record: records){
                tmp = new HashMap<>();
                tmp.put("id", record.getId());
                tmp.put("content", record.getContent());
                tmp.put("saveTime", record.getSaveTime());
                //   tmp.put("judgeTime", record.getJudgeTime());
                tmp.put("reason", record.getAdmitReason());
                tmp.put("modifyReason", record.getModifyReason());
                tmp.put("name", record.getEntryName());
                tmp.put("field", record.getField());
                tmp.put("source", 1);
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
     * 获取词条task内容或Record内容
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/user/getTaskContent")
    @CrossOrigin
    public ResponseEntity<?> getTaskContent(HttpServletRequest request, @RequestBody String jsonParam) {
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer taskId = form.getInteger("taskId");
            Integer source = form.getInteger("source");
            if (source == 2) {
                JSONObject task = this.subjectManagementService.getTask(userId, taskId);
                return new ResponseEntity<>(BaseResultFactory.build(task, "success"), HttpStatus.OK);
            } else if (source == 1) {
                JSONObject record = this.subjectManagementService.getRecord(userId, taskId);
                return new ResponseEntity<>(BaseResultFactory.build(record, "success"), HttpStatus.OK);
            } else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (MyException me) {
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(), me.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 暂存词条task或者记录Record
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
            Integer type = data.getInteger("type");
            JSONObject form = data.getJSONObject("form");
            String entryName = form.getString("entryName");
            Integer originalId = form.getInteger("originId");
            String imageUrl = form.getString("imageUrl");
            JSONArray field = form.getJSONArray("field");
            String intro = form.getString("intro");
            JSONArray infoBox = form.getJSONArray("infoBox");
            String content = form.getString("content");
            JSONArray reference = form.getJSONArray("reference");
            JSONArray rel = form.getJSONArray("relation");
            if(type == 2)
                subjectManagementService.saveTask(userId, taskId, entryName, imageUrl, field, intro, infoBox, content, reference, rel);
            else if(type == 1)
                subjectManagementService.saveRecord(userId, taskId, entryName, originalId, imageUrl, field, intro, infoBox, content, reference, rel);
            return new ResponseEntity<>(BaseResultFactory.build("编辑成功"), HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),me.getMessage()),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"编辑错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户提交词条task或记录Record
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
            String isTask = form.getString("isTask");
            int len = entryIds.size();
            if(isTask == "true") {
                for (int i = 0; i < len; i++) {   //每次只有一条，懒得改了
                    Integer taskId = entryIds.getInteger(i);
                    subjectManagementService.submitTask(userId, taskId, reason);
                }
            }else{
                for (int i = 0; i < len; i++) {
                    Integer taskId = entryIds.getInteger(i);
                    subjectManagementService.submitRecord(userId, taskId, reason);
                }
            }
            return new ResponseEntity<>(BaseResultFactory.build("提交成功"), HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),me.getMessage()),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"提交错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户放弃任务或者词条编辑
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
            List<String> isTasks = (List<String>) form.get("isTaskArray");
            for (int i = 0; i < entryIds.size(); i++){
                Integer taskId = entryIds.get(i);
                String isTask = isTasks.get(i);
                if(isTask == "true") {
                    Task task = taskRepository.findTaskById(taskId);
                    Assignment assignment = task.getAssignment();
                    assignment.setState(Assignment.PUBLISHED);
                    assignmentRepository.save(assignment);
                    taskRepository.delete(task);
                }else{
                    Record record = recordRepository.findRecordById(taskId);
                    recordRepository.delete(record);
                }
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
            subjectManagementService.drawAssignment(userId, assignmentId);
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
            List<Object> tmps = new ArrayList<>();
            JSONObject tmp = null;
            for(GroupMember groupMember : groupMembers){
                Subject subject = groupMember.getSubject();
                tmp = new JSONObject();
                tmp.put("id", subject.getId());
                tmp.put("name", subject.getName());
                tmp.put("imgUrl", subject.getImageUrl());
                tmp.put("deadline", subject.getDeadline());
                tmp.put("finishNum", subject.getCurrentCount());
                tmp.put("introduction", subject.getIntroduction());
                tmps.add(tmp);
            }
            JSONObject result = new JSONObject();
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
            JSONArray tmps = new JSONArray();
            JSONObject tmp = null;
            if(subjects.size() != 0){
                for(Subject subject: subjects){
                    List<GroupMember> groupMembers = groupMemberRepository.findAllBySubject_IdAndIdentity(subject.getId(), GroupMember.ORDINRYUSER);
                    tmp = new JSONObject();
                    tmp.put("id", subject.getId());
                    tmp.put("name", subject.getName());
                    tmp.put("field", subject.getField());
                    tmp.put("deadTime", subject.getDeadline());
                    tmp.put("memberCount", groupMembers.size());
                    tmp.put("total_count", subject.getTotalCount());
                    tmps.add(tmp);
                }
                JSONObject result = new JSONObject();
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
            String reasonResult [] = null;
            if(assignments.size() != 0){
                for(Assignment assignment: assignments){
                    tmp = new JSONObject();
                    tmp.put("id", assignment.getId());
                    tmp.put("name", assignment.getEntryName());
                    tmp.put("field", assignment.getField());
                    tmp.put("reason", assignment.getModifyReason());
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
                    List<GroupMember> groupMembers = groupMemberRepository.findAllBySubject_IdAndIdentity(subject.getId(), GroupMember.ORDINRYUSER);
                    tmp = new JSONObject();
                    tmp.put("id", subject.getId());
                    tmp.put("name", subject.getName());
                    tmp.put("field", subject.getField());
                    tmp.put("intro", subject.getIntroduction());
                    tmp.put("deadTime", subject.getDeadline());
                    tmp.put("total_count", subject.getTotalCount());
                    tmp.put("memberCount", groupMembers.size());
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
            String reasonResult [] = null;
            if(assignments.size() != 0){
                for(Assignment assignment: assignments){
                    tmp = new HashMap<>();
                    tmp.put("id", assignment.getId());
                    tmp.put("name", assignment.getEntryName());
                    tmp.put("field", assignment.getField());
                    tmp.put("reason",assignment.getModifyReason());
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

    /**
     * 用户是否已有词条记录
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/user/exitUserRecord")
    @CrossOrigin
    public ResponseEntity<?> exitUserRecord(HttpServletRequest request,@RequestBody String jsonParam){
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer originId = form.getInteger("originId");
            Record record = recordRepository.findByOriginalIdAnAndUser(originId, userId);
            JSONObject result = new JSONObject();
            if(record != null){
                if(record.getState() == Record.DRAWED) {
                    result.put("id", record.getId());
                    result.put("state", 1);
                }else if(record.getState() == Record.TOAUDITED) {
                    result.put("state", 2);
                }
            }else{
                result.put("state", 0);
            }
            return new ResponseEntity<>(BaseResultFactory.build(result,"success"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户申请权限
     * @param request
     * @return
     */
    @PostMapping("/api/user/applyAuthority")
    @CrossOrigin
    public ResponseEntity<?> applyAuthority(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            Integer userId = (Integer)request.getAttribute("userId");
            JSONObject form = JSONObject.parseObject(jsonParam);
            Integer affair = form.getInteger("affair");
            User user = userRepository.findUserById(userId);
            if (user != null) {
                Application application = new Application(affair, user);
                applicationRepository.save(application);
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.OK.value(),"申请成功"), HttpStatus.OK);
            }else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户未注册"), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"网络错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 用户创建新词条暂存Record
     * @param request
     * @param jsonParam
     * @return
     */
    @PostMapping("api/user/createEntry")
    @CrossOrigin
    public ResponseEntity<?> createEntry(HttpServletRequest request, @RequestBody String jsonParam) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            JSONObject data = JSONObject.parseObject(jsonParam);
            String entryName = data.getString("entryName");
            JSONArray field = data.getJSONArray("field");
            Integer recordId = subjectManagementService.saveRecord(userId, -1, entryName, -1, "", field, "", new JSONArray(), "", new JSONArray(), new JSONArray());
            JSONObject result = new JSONObject();
            result.put("id", recordId);
            return new ResponseEntity<>(BaseResultFactory.build(result, "创建成功"), HttpStatus.OK);
        }catch (MyException me){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),me.getMessage()),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"创建错误"),HttpStatus.BAD_REQUEST);
        }
    }

}