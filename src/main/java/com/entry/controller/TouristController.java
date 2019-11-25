package com.entry.controller;

import com.entry.util.JwtUtil;
import com.entry.entity.mysql.User;
import com.entry.repository.mysql.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.entry.dto.BaseResultFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

/*
  游客, 不经过身份认证
 */
@RestController
public class TouristController {

    @Autowired
    UserRepository userRepository;


    @PostMapping("/api/tourist/login")
    @CrossOrigin
    public ResponseEntity<?> touristLogin(@RequestBody String jsonParam){
        try{
            HashMap<String,String> user_data = new ObjectMapper().readValue(jsonParam,HashMap.class);
            String username=user_data.get("username");
            String password=user_data.get("password");
            User user = userRepository.findUserByName(username);
            if (user != null)
                if (password.equals(user.getPassword())) {
                    String token = JwtUtil.getToken(user.getId(), user.getAuthorith());
                    HashMap<String,String> result=new HashMap<>();
                    result.put("token", token);
                    result.put("name", user.getName());
                    result.put("status", user.getAuthorith().toString());
                    return new ResponseEntity<>(BaseResultFactory.build(result,"登陆成功"), HttpStatus.OK);
                }else
                    return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户密码错误"), HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.NOT_FOUND.value(),"用户未注册"), HttpStatus.NOT_FOUND);
        }catch (IOException e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/tourist/register")
    @CrossOrigin
    public ResponseEntity<?> touristRegister(@RequestBody String jsonParam){
        try{
            HashMap<String,String> user_data = new ObjectMapper().readValue(jsonParam,HashMap.class);
            String username = user_data.get("username");
            String password = user_data.get("password");
            String email = user_data.get("email");
            User user = new User(username,password,1,null,email);
            try{
                userRepository.save(user);
                return new ResponseEntity<>(BaseResultFactory.build("用户注册成功"), HttpStatus.OK);
            }catch (Exception e){
                System.out.println(e.getMessage());
                return new ResponseEntity<>(BaseResultFactory.build(e.getMessage()), HttpStatus.NOT_FOUND);
            }
        }catch (IOException e){
            return new ResponseEntity<>(BaseResultFactory.build(HttpStatus.BAD_REQUEST.value(),"输入错误"),HttpStatus.BAD_REQUEST);
        }
    }

}
