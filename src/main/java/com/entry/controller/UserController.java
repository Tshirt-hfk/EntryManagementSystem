package com.entry.controller;

import com.entry.entity.mysql.User;
import com.entry.entity.mysql.Task;
import com.entry.entity.mysql.Assignment;
import com.entry.repository.mysql.UserRepository;
import com.entry.repository.mysql.TaskRepository;
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

}
