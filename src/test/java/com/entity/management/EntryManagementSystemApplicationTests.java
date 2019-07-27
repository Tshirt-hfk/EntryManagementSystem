package com.entity.management;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.Subject;
import com.entry.entity.mysql.GroupMember;
import com.entry.entity.mysql.Task;
import com.entry.entity.mysql.User;
import com.entry.repository.mysql.*;
import com.entry.util.HttpRequestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.entry.EntryManagementSystemApplication;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntryManagementSystemApplication.class)
public class EntryManagementSystemApplicationTests {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    GroupMemberRepository subjectGroupRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    public void contextLoads() {
        try {
            String url = "http://192.168.1.120:5005/keywords_extraction";
            JSONObject jsonObject = new JSONObject();
            String data = HttpRequestUtil.post(url,"");
            JSONObject result = JSONObject.parseObject(data);
            JSONArray nodes = result.getJSONArray("nodes");
            int len = nodes.size();
            for(int i=0;i<len;i++){
                JSONObject node = nodes.getJSONObject(i);
                String entryName = node.getString("name");
//                Integer originId = node.getInteger("originId");
                System.out.println(entryName);
            }
        }catch (Exception e){

        }

    }

}
