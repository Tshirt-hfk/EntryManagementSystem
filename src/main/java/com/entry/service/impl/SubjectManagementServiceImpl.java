package com.entry.service.impl;

import com.entry.entity.mysql.*;
import com.entry.exception.MyException;
import com.entry.repository.mysql.AssignmentRepository;
import com.entry.repository.mysql.GroupMemberRepository;
import com.entry.service.SubjectManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("subjectManagementServiceImpl")
public class SubjectManagementServiceImpl implements SubjectManagementService {

    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Override
    public void createAssignment(User user, Subject subject, String Entryname, List<String> fieldList) throws MyException {
        // TODO 判断该词条是否以及在数据库中
        this.testIdentity(user,subject,GroupMember.SUBJECTMAKER);
        String fieldStr = fieldList.stream().collect(Collectors.joining(","));
        Assignment assignment = new Assignment(Entryname,"",fieldStr,Assignment.UNPUBLISHED,subject);
        assignmentRepository.save(assignment);
    }
    @Override
    public void publishAssignment(User user, Subject subject, String reason, Long deadline) throws MyException  {

    }
    @Override
    public void drawAssignment(User user, Subject subject, Assignment assignment) throws MyException  {

    }
    @Override
    public void saveTask(User user, Subject subject, Task task, String content) throws MyException {

    }
    @Override
    public void submitTask(User user, Subject subject, Task task, String content) throws MyException {

    }
    @Override
    public void auditTask(User user, Subject subject, Task task) throws MyException {

    }


    private void testIdentity(User user, Subject subject, Integer identity) throws MyException {
        if(user==null){
            throw new MyException("用户不存在！");
        }
        if(subject==null){
           throw  new MyException("专题不存在！");
        }
        GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(user.getId(),subject.getId());
        if(groupMember==null){
            throw new MyException("用户未加入该专题！");
        }
        Integer auth = groupMember.getIdentity();
        if(identity!=auth){
            if(identity==GroupMember.SUBJECTMAKER)
                throw new MyException("用户不是该专题的管理人！");
        }
    }
}
