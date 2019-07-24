package com.entry.service.impl;

import com.entry.dto.BaseResultFactory;
import com.entry.entity.mysql.*;
import com.entry.exception.MyException;
import com.entry.repository.mysql.*;
import com.entry.service.SubjectManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("subjectManagementServiceImpl")
public class SubjectManagementServiceImpl implements SubjectManagementService {
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

    @Override
    public void createAssignment(User user, Subject subject, String Entryname, List<String> fieldList) throws MyException {
        // TODO 判断该词条是否以及在数据库中
        this.testIdentity(user,subject,GroupMember.SUBJECTMAKER);
        String fieldStr = fieldList.stream().collect(Collectors.joining(","));
        Assignment assignment = new Assignment(Entryname,"",fieldStr,Assignment.UNPUBLISHED,subject);
        assignmentRepository.save(assignment);
    }
    @Override
    public void publishAssignment(User user, Subject subject, String reason, Integer deadline, List<Integer> entryIds) throws MyException  {
        this.testIdentity(user, subject, GroupMember.SUBJECTMAKER);
        Integer num = 0;
        try {
            for (Integer id : entryIds) {
                Assignment assignment = assignmentRepository.findAssignmentById(id);
                assignment.setState(Assignment.PUBLISHED);
                assignment.setDeadline(deadline);
                assignment.setModifyReason(reason);
                assignmentRepository.save(assignment);
                num = num + 1;
            }
        }catch (Exception e){
            // TODO logo
        }finally {
            subject.setTotalCount(subject.getTotalCount() + num);
            subjectRepository.save(subject);
        }
    }
    @Override
    public void drawAssignment(User user, Subject subject, Assignment assignment) throws MyException  {
        this.testIdentity(user, subject, GroupMember.ORDINRYUSER);
        assignment.setState(Assignment.DRAWED);
        Task task = new Task(subject,user,assignment,Task.DRAWED);
        task.setSaveTime(new Date().getTime());
        task.setJudgeTime(new Date().getTime());
        assignmentRepository.save(assignment);
        taskRepository.save(task);
    }
    @Override
    public void saveTask(User user, Subject subject, Task task, String content) throws MyException {
        this.testIdentity(user, subject, GroupMember.ORDINRYUSER);
        task.setSaveTime(new Date().getTime());
        task.setContent(content);
        taskRepository.save(task);
    }
    @Override
    public void submitTask(User user, Subject subject, Task task, String content, String reason) throws MyException {
        this.testIdentity(user, subject, GroupMember.ORDINRYUSER);
        Assignment assignment = task.getAssignment();
        task.setState(Task.TOAUDITED);
        task.setSaveTime(new Date().getTime());
        task.setAdmitReason(reason);
        assignment.setState(Assignment.TOAUDITED);
        assignment.setContent(content);
        taskRepository.save(task);
        assignmentRepository.save(assignment);
    }
    @Override
    public void auditTask(User user, Subject subject, Task task, Boolean pass, String reason) throws MyException {
        this.testIdentity(user, subject, GroupMember.SUBJECTMAKER);
        Assignment assignment = task.getAssignment();
        GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(user.getId(), subject.getId());
        if(pass){
            assignment.setState(Assignment.TOSUBMIT);
            assignment.setContent(task.getContent());
            task.setJudgeTime(new Date().getTime());
            task.setState(Task.PASS);
            taskRepository.save(task);
            assignmentRepository.save(assignment);
            groupMember.setMyCompletedCount(groupMember.getMyCompletedCount()+1);
            groupMemberRepository.save(groupMember);
            subject.setCurrentCount(subject.getCurrentCount()+1);
            subjectRepository.save(subject);
        }else{
            assignment.setState(Assignment.PUBLISHED);
            task.setJudgeTime(new Date().getTime());
            task.setAdmitReason(reason);
            task.setState(Task.UNPASS);
            assignmentRepository.save(assignment);
            taskRepository.save(task);
        }
    }


    private void testIdentity(User user, Subject subject, Integer identity) throws MyException {
        if(user == null){
            throw new MyException("用户不存在！");
        }
        if(subject == null){
           throw  new MyException("专题不存在！");
        }
        GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(user.getId(),subject.getId());
        if(groupMember == null){
            throw new MyException("用户未加入该专题！");
        }
        Integer auth = groupMember.getIdentity();
        if(identity != auth){
            if(identity == GroupMember.SUBJECTMAKER)
                throw new MyException("用户不是该专题的管理人！");
        }
    }
}
