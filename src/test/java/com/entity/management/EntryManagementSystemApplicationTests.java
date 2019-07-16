package com.entity.management;

import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.Subject;
import com.entry.entity.mysql.GroupMember;
import com.entry.entity.mysql.Task;
import com.entry.entity.mysql.User;
import com.entry.repository.mysql.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.entry.EntryManagementSystemApplication;

import java.sql.Timestamp;

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


    }

}
