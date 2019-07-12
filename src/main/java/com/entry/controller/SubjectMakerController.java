package com.entry.controller;

import com.entry.dto.BaseResultFactory;
import com.entry.entity.mysql.*;
import com.entry.entity.mysql.pk.GroupMemberPK;
import com.entry.repository.mysql.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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

    @Autowired
    TaskRepository taskRepository;


    @PostMapping("/api/subjectMaker/getSubject")
    @CrossOrigin
    public ResponseEntity<?> getSubject(HttpServletRequest request){
        try{
            System.out.println("test");
            Integer userId = (Integer)request.getAttribute("userId");
            System.out.println(userId);
            List<GroupMember> groupMembers = groupMemberRepository.findAllByUserAndIdentity(userId,GroupMember.SUBJECTMAKER);
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
            //
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
            String field = (String) form.get("field");
            Integer subjectId = (Integer)form.get("subjectId");
            // TODO 判断该词条是否已经被创建
            Subject subject = subjectRepository.findSubjectById(subjectId);
            Assignment assignment = new Assignment(name,"",field,Assignment.UNPUBLISHED,subject);
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
            List<Assignment> assignments = assignmentRepository.findAllBySubjectAndState(subjectId,type);
            System.out.println(assignments.size());
            HashMap<String,Object> result1;
            List<Object> list = new ArrayList<>();
            for(Assignment assignment : assignments){
                result1=new HashMap<>();
                result1.put("id",assignment.getId());
                result1.put("name",assignment.getEntryName());
                result1.put("field",assignment.getField());
                if(type==3 || type==4){
                    Task task = assignment.getTask();
                    User user = task.getUser();
                    result1.put("username", user.getName());
                    result1.put("userId", user.getId());
                }
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
     * 发布任务
     * @param jsonParam
     * @return
     */
    @PostMapping("/api/subjectMaker/publishAssignment")
    @CrossOrigin
    @Transactional
    public ResponseEntity<?> publishAssignment(HttpServletRequest request, @RequestBody String jsonParam){
        try{
            System.out.println(jsonParam);
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            System.out.println(form);
            List<Integer> entryIds = (List<Integer>) form.get("entryIds");
            for(Integer id : entryIds){
                System.out.println(id);
                assignmentRepository.updateStateById(Assignment.PUBLISHED,id);
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
            Integer assignmentId = (Integer) form.get("assignmentId");
            Boolean pass = (Boolean)form.get("pass");
            Integer userId = (Integer)form.get("userId");
            Task task = taskRepository.findTaskByAssignment_IdAAndUser_Id(assignmentId, userId);
            Assignment assignment = assignmentRepository.findAssignmentById(assignmentId);
            if(assignment != null && task != null) {
                if(pass){
                    assignment.setState(Assignment.TOSUBMIT);
                    assignment.setContent(task.getContent());
                    task.setState(Task.TOSUBMIT);
                    taskRepository.save(task);
                    assignmentRepository.save(assignment);
                    return new ResponseEntity<>(BaseResultFactory.build("审核通过"), HttpStatus.OK);
                }else{
                    // TODO
                    assignment.setState(Assignment.PUBLISHED);
                    assignmentRepository.save(assignment);
                    taskRepository.delete(task);
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
