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

        User user = new User("fl", "13866079469",0,"13866079469","1258595753@qq.com");
        Subject subject = new Subject("生物学领域","生物学领域专题","生物学");
        GroupMember gm = new GroupMember(subject, user,0);
        Assignment assignment = new Assignment("遗传学","......","遗传",subject);
        Task task = new Task(subject,user,assignment,0,"......",new Timestamp(100000000));


    }

}
