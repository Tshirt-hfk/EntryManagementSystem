package com.entry.controller;

import com.entry.entity.mysql.User;
import com.entry.entity.mysql.Task;
import com.entry.entity.mysql.Assignment;
import com.entry.repository.mysql.UserRepository;
import com.entry.repository.mysql.TaskRepository;
import com.entry.dto.BaseResultFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("api/user/entry/get")
    @CrossOrigin
    public ResponseEntity<?> getEntryByName(HttpServletRequest request) {
        try{
            String user_name = (String)request.getParameter("name");
            User user = userRepository.findUserByName(user_name);
            List<Task> tasks = user.getTaskList();
   //         List<Task> tasks = taskRepository.findTaskByUserId(user.getId());
            if(tasks.size() != 0){
                List<HashMap<String,Object>> list = new ArrayList<>();
                HashMap<String, Object> result1 = new HashMap<>();
                HashMap<String, Object> result2 = new HashMap<>();
                HashMap<String, Object> result3 = new HashMap<>();
                for(Task task: tasks){
                    Assignment assignment = task.getAssignment();
                    Integer state = assignment.getState();
                    if(state == Assignment.DRAWED){
                        result1.put("name", assignment.getEntryName());
                    }else if(state == Assignment.TOAUDITED){
                        result2.put("name", assignment.getEntryName());
                    }else{
                        result3.put("name", assignment.getEntryName());
                    }
                }
                list.add(result1);
                list.add(result2);
                list.add(result3);
                HashMap<String, Object> result = new HashMap<>();
                result.put("assignments", list);
                return new ResponseEntity<>(BaseResultFactory.build(result), HttpStatus.OK);
            }else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户没有词条"), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"我真的错了"),HttpStatus.BAD_REQUEST);
        }
    }

}
