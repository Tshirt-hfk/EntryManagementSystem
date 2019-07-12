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


    @PostMapping("/api/subjectmaker/getSubject")
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
    @PostMapping("/api/subjectmaker/subject/create")
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

    /**
     * 获取该专题下的任务
     * @param id
     * @return
     */
    @GetMapping("/api/subjectmaker/assignment/get")
    @CrossOrigin
    public ResponseEntity<?> getSubject(@RequestParam("id") Integer id){
        try{
            System.out.println(id);
            Subject subject = subjectRepository.findSubjectById(id);
            List<Assignment> assignments = subject.getAssignmentlist();
            HashMap<String,Object> result1=new HashMap<>();
            HashMap<String,Object> result2=new HashMap<>();
            HashMap<String,Object> result3=new HashMap<>();
            HashMap<String,Object> result4=new HashMap<>();
            HashMap<String,Object> result5=new HashMap<>();
            for(Assignment assignment : assignments){
                Integer state = assignment.getState();
                if(state==Assignment.UNPUBLISHED){
                    result1.put("name",assignment.getEntryName());
                    result1.put("id",assignment.getId());
                }else if(state==Assignment.PUBLISHED){
                    result2.put("name",assignment.getEntryName());
                    result2.put("id",assignment.getId());
                }else if(state==Assignment.DRAWED){
                    result3.put("name",assignment.getEntryName());
                    result3.put("id",assignment.getId());
                }else if(state==Assignment.TOAUDITED){
                    result4.put("name",assignment.getEntryName());
                    result4.put("id",assignment.getId());
                }else{
                    result5.put("name",assignment.getEntryName());
                    result5.put("id",assignment.getId());
                }
            }
            System.out.println("test");
            List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
            list.add(result1);
            list.add(result2);
            list.add(result3);
            list.add(result4);
            list.add(result5);
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
    @PostMapping("/api/subjectmaker/assignment/publish")
    @CrossOrigin
    public ResponseEntity<?> publishAssignment(@RequestBody String jsonParam){
        try{
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer id=(Integer) form.get("id");
            Assignment assignment = assignmentRepository.findAssignmentById(id);
            if(assignment != null) {
                assignment.setState(Assignment.PUBLISHED);
                assignmentRepository.save(assignment);
                return new ResponseEntity<>(BaseResultFactory.build("发布成功"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("无效专题"), HttpStatus.NOT_FOUND);
            }
        }catch (IOException e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 审核通过
     */
    @PostMapping("/api/subjectmaker/task/audit")
    @CrossOrigin
    public ResponseEntity<?> auditAssignment(@RequestBody String jsonParam){
        try{
            HashMap<String,Object> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
            Integer id=(Integer) form.get("id");
            Assignment assignment = assignmentRepository.findAssignmentById(id);
            Task task = assignment.getTask();
            if(assignment != null && task != null) {
                assignment.setState(Assignment.TOSUBMIT);
                assignment.setContent(task.getContent());
                assignment.setField(task.getField());
                taskRepository.delete(task);
                return new ResponseEntity<>(BaseResultFactory.build("发布成功"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(BaseResultFactory.build("无效专题"), HttpStatus.NOT_FOUND);
            }

        }catch (IOException e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }




}
