package com.entry.controller;

import com.alibaba.fastjson.JSONObject;
import com.entry.dto.BaseResultFactory;
import com.entry.entity.mysql.*;
import com.entry.entity.mysql.pk.GroupMemberPK;
import com.entry.repository.mysql.*;
import com.entry.util.HttpRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Timestamp;
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

    /**
     * 获取所有的专题的信息列表
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
            System.out.println(jsonParam);
            Integer userId = (Integer)request.getAttribute("userId");
            User user = userRepository.findUserById(userId);
            // 创建专题组
            System.out.println(jsonParam);
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            String imageUrl=(String)form.getOrDefault("imageUrl","");
            String name=(String)form.get("name");
            List<String> fields = (ArrayList<String>) form.get("field");
            String fieldStr = "";
            int len = fields.size();
            for(int i=0;i<len-1;i++){
                fieldStr = fieldStr + fields.get(i);
            }
            fieldStr = fieldStr + fields.get(len-1);
            Boolean isPublic=(Boolean)form.get("isPublic");
            String introduction=(String)form.get("introduction");
            String goal=(String)form.get("goal");
            Long deadLine= (Long)form.getOrDefault("deadline", (new Date()).getTime()+3600*1000*24*30);
            Subject subject = new Subject(imageUrl,name,user.getName(),introduction,goal,fieldStr,new Timestamp(deadLine),isPublic);
            // 将创建人加入专题组中
            GroupMember groupMember = new GroupMember(new GroupMemberPK(subject,user),2);
            subjectRepository.save(subject);
            groupMemberRepository.save(groupMember);
            //初始化 专题的所有任务
//            String content = HttpRequestUtil.get("http://localhost:5003/keywords_extraction");
//            JSONObject data = JSONObject.parseObject(content);
//            List<JSONObject> nodes = JSONObject.parseArray(data.getJSONArray("nodes").toJSONString(), JSONObject.class);
//            for(JSONObject node : nodes){
//                String entryName = node.getString("name");
//                System.out.println(entryName);
//                Assignment assignment = new Assignment(entryName,"","",Assignment.UNPUBLISHED,subject);
//                assignmentRepository.save(assignment);
//            }
            return new ResponseEntity<>(BaseResultFactory.build("创建成功"), HttpStatus.OK);
        }catch (IOException e){
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
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            String name = (String) form.get("name");
            List<String> fields = (ArrayList<String>) form.get("field");
            String fieldStr = "";
            int len = fields.size();
            for(int i=0;i<len-1;i++){
                fieldStr = fieldStr + fields.get(i);
            }
            fieldStr = fieldStr + fields.get(len-1);
            Integer subjectId = (Integer)form.get("subjectId");
            // TODO 判断该词条是否已经被创建
            Subject subject = subjectRepository.findSubjectById(subjectId);
            Assignment assignment = new Assignment(name,"",fieldStr,Assignment.UNPUBLISHED,subject);
            assignmentRepository.save(assignment);
            return new ResponseEntity<>(BaseResultFactory.build("创建成功"), HttpStatus.OK);
        }catch (IOException e){
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

            HashMap<String,Object> result1;
            List<Object> list = new ArrayList<>();
            if(type>=Assignment.DRAWED){
                List<Task> tasks = taskRepository.findAllBySubject_IdAndState(subjectId,type);
                for(Task task : tasks) {
                    User user = task.getUser();
                    result1 = new HashMap<>();
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
                    result1=new HashMap<>();
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
     * 发布任务
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/subjectMaker/publishAssignment")
    @CrossOrigin
    public ResponseEntity<?> publishAssignment(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            System.out.println(jsonParam);
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            System.out.println(form);
            List<Integer> entryIds = (List<Integer>) form.get("entryIds");
            Integer subjectId = (Integer) form.get("subjectId");
            Integer time = (Integer) form.getOrDefault("dealine", 10*24*3600*1000);
            Subject subject = subjectRepository.findSubjectById(subjectId);
            Integer num = 0;
            try {
                for (Integer id : entryIds) {
                    Assignment assignment = assignmentRepository.findAssignmentById(id);
                    assignment.setState(Assignment.PUBLISHED);
                    assignment.setDeadline(time);
                    assignmentRepository.save(assignment);
                    num = num + 1;
                }
            }catch (Exception e){
                // TODO logo
            }finally {
                subject.setTotalCount(subject.getTotalCount()+num);
                subjectRepository.save(subject);
            }
            return new ResponseEntity<>(BaseResultFactory.build("发布成功"), HttpStatus.OK);
        }catch (IOException e){
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
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Boolean pass = (Boolean)form.get("pass");
            Integer userId = (Integer)form.get("userId");
            Integer taskId = (Integer)form.get("taskId");
            Task task = taskRepository.findTaskById(taskId);
            Assignment assignment = task.getAssignment();
            Subject subject = task.getSubject();
            System.out.println(userId);
            if(task == null)
                System.out.println("haha");;
            GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(userId,subject.getId());
            if(assignment != null && task != null) {
                if(pass){
                    assignment.setState(Assignment.TOSUBMIT);
                    assignment.setContent(task.getContent());
                    task.setJudgeTime(new Date().getTime());
                    task.setState(Task.PASS);
                    taskRepository.save(task);
                    assignmentRepository.save(assignment);
                    groupMember.setMyCompletedCount(groupMember.getMyCompletedCount()+1);
                    groupMemberRepository.save(groupMember);
                    subject.setCurrentCount(subject.getCurrentCount()+1);
                    subjectRepository.save(subject);
                    return new ResponseEntity<>(BaseResultFactory.build("审核通过"), HttpStatus.OK);
                }else{
                    // TODO
                    String reason = (String) form.get("reason");
                    assignment.setState(Assignment.PUBLISHED);
                    assignmentRepository.save(assignment);
                    task.setJudgeTime(new Date().getTime());
                    task.setAdmitReason(reason);
                    task.setState(Task.UNPASS);
                    taskRepository.save(task);
                    return new ResponseEntity<>(BaseResultFactory.build("审核不通过"), HttpStatus.OK);
                }
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("无效专题"), HttpStatus.NOT_FOUND);
            }
        }catch (IOException e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

}