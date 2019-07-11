package com.entry.service;

import com.entry.entity.mysql.Subject;
import com.entry.util.HttpRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service("httpRequestService")
public class HttpRequestService {

//    public boolean createSubject(String jsonParam){
//        try{
//            HashMap<String,String> form = new ObjectMapper().readValue(jsonParam,HashMap.class);
//            String name=form.get("name");
//            String field=form.get("field");
//            boolean isPublic;
//            if(form.get("isPublic")=="true"){
//                isPublic = true;
//            }else{
//                isPublic = false;
//            }
//            String introduction=form.get("introduction");
//            String goal=form.get("goal");
//            Subject subject = new Subject(name,introduction,goal,field,isPublic);
//
//            String response =  HttpRequestUtil.get("https://blog.csdn.net/wanghang88/article/details/80115025");
//            HashMap<String,String> responseJson = new ObjectMapper().readValue(jsonParam,HashMap.class);
//            try{
//
//            }catch (Exception excption){
//
//            }
//
//        }catch (IOException e){
//
//        }
//    }

}
