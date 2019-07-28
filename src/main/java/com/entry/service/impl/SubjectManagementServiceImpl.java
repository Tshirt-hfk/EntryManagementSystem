package com.entry.service.impl;

import com.alibaba.fastjson.JSONArray;
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

    /**
     * 创建词条任务
     * @param userId
     * @param subjectId
     * @param EntryName
     * @param field
     * @throws MyException
     */
    @Override
    public void createAssignment(Integer userId, Integer subjectId, String EntryName, JSONArray field) throws MyException {
        // TODO 判断该词条是否以及在数据库中
        User user = this.testUser(userId);
        Subject subject = this.testSubject(subjectId);
        this.testSubjectMaker(user,subject);
        Assignment assignment = new Assignment(EntryName,field,subject);
        assignmentRepository.save(assignment);
    }

    /**
     * 发布词条任务
     * @param userId
     * @param subjectId
     * @param reason
     * @param deadline
     * @param entryIds
     * @throws MyException
     */
    @Override
    public void publishAssignment(Integer userId, Integer subjectId, String reason, Integer deadline, List<Integer> entryIds) throws MyException  {
        User user =  this.testUser(userId);
        Subject subject = this.testSubject(subjectId);
        this.testSubjectMaker(user, subject);
        Integer num = 0;
        for (Integer id : entryIds) {
            Assignment assignment = assignmentRepository.findAssignmentById(id);
            assignment.setState(Assignment.PUBLISHED);
            assignment.setDeadline(deadline);
            assignment.setModifyReason(reason);
            assignmentRepository.save(assignment);
            num = num + 1;
        }
        subject.setTotalCount(subject.getTotalCount() + num);
        subjectRepository.save(subject);
    }

    /**
     * 领取专题任务
     * @param userId
     * @param assignmentId
     * @throws MyException
     */
    @Override
    public void drawAssignment(Integer userId, Integer assignmentId) throws MyException  {
        User user  = this.testUser(userId);
        Assignment assignment = this.testAssignment(assignmentId);
        Subject subject = assignment.getSubject();
        this.testOrdinaryUser(user,subject);
        List<Task> tasks = taskRepository.findAllBySubject_IdAndUser_IdAndState(subject.getId(),user.getId(),Task.DRAWED);
        int sum = 0;
        for(Task task : tasks){
            try {
                this.testTaskOutOfDate(task);
                sum = sum + 1;
            }catch (MyException me){

            }
        }
        if(sum >= subject.getDrawedTaskCountLimit())
            throw new MyException("用户领取的词条数量达到上限");
        assignment.setState(Assignment.DRAWED);
        Task task = new Task(subject,user,assignment);
        task.setSaveTime(new Date().getTime());
        task.setJudgeTime(new Date().getTime());
        assignmentRepository.save(assignment);
        taskRepository.save(task);
    }

    /**
     * 暂存词条任务
     * @param userId
     * @param taskId
     * @param entryName
     * @param imageUrl
     * @param field
     * @param intro
     * @param infoBox
     * @param content
     * @param reference
     * @throws MyException
     */
    @Override
    public void saveTask(Integer userId, Integer taskId, String entryName, String imageUrl, JSONArray field, String intro, JSONArray infoBox, String content, JSONArray reference) throws MyException {
        User user = this.testUser(userId);
        Task task = this.testTask(taskId);
        if(task.getUser().getId() != user.getId())
            throw new MyException("用户不是任务的领取人！");
        if(task.getState()==Task.TOAUDITED)
            throw new MyException("任务待审核状态，不能被修改！");
        task.setSaveTime(new Date().getTime());
        task.setEntryName(entryName);
        task.setImageUrl(imageUrl);
        task.setField(field);
        task.setIntro(intro);
        task.setInfoBox(infoBox);
        task.setContent(content);
        taskRepository.save(task);
        this.testTaskOutOfDate(task);
    }

    /**
     * 提交词条任务
     * @param userId
     * @param taskId
     * @param reason
     * @throws MyException
     */
    @Override
    public void submitTask(Integer userId, Integer taskId, String reason) throws MyException {
        User user =  this.testUser(userId);
        Task task =  this.testTask(taskId);
        this.testOrdinaryUser(user, task.getSubject());
        if(task.getUser().getId() != user.getId())
            throw new MyException("用户不是任务的领取人！");
        this.testTaskOutOfDate(task);
        Assignment assignment = task.getAssignment();
        task.setState(Task.TOAUDITED);
        task.setSaveTime(new Date().getTime());
        task.setAdmitReason(reason);
        assignment.setState(Assignment.TOAUDITED);
        taskRepository.save(task);
        assignmentRepository.save(assignment);
    }

    /**
     * 审核词条任务
     * @param userId
     * @param taskId
     * @param pass
     * @param reason
     * @throws MyException
     */
    @Override
    public void auditTask(Integer userId, Integer taskId, Boolean pass, String reason) throws MyException {
        User user = this.testUser(userId);
        Task task = this.testTask(taskId);
        Subject subject = task.getSubject();
        GroupMember groupMember = this.testSubjectMaker(user, subject);
        Assignment assignment = task.getAssignment();
        if(pass){
            assignment.setEntryName(task.getEntryName());
            assignment.setImageUrl(task.getImageUrl());
            assignment.setIntro(task.getIntro());
            assignment.setInfoBox(task.getInfoBox());
            assignment.setContent(task.getContent());
            assignment.setState(Assignment.TOSUBMIT);;
            task.setJudgeTime(new Date().getTime());
            task.setState(Task.PASS);
            groupMember.setMyCompletedCount(groupMember.getMyCompletedCount()+1);
            subject.setCurrentCount(subject.getCurrentCount()+1);
            taskRepository.save(task);
            assignmentRepository.save(assignment);
            groupMemberRepository.save(groupMember);
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

    private void testTaskOutOfDate(Task task) throws MyException {
        if(task.getState()==Task.OUTOFDATE){
            throw new MyException("任务已过期!");
        }
        else if(task.getState()==Task.DRAWED && task.getDeadline()<(new Date()).getTime()){
            task.setState(Task.OUTOFDATE);
            Assignment assignment = task.getAssignment();
            assignment.setState(Assignment.PUBLISHED);
            taskRepository.save(task);
            assignmentRepository.save(assignment);
            throw new MyException("任务已过期!");
        }
    }

    private User testUser(Integer userId) throws MyException  {
        User user = userRepository.findUserById(userId);
        if(user == null)
            throw new MyException("用户不存在！");
        return  user;
    }

    private Subject testSubject(Integer subjectId) throws MyException {
        Subject subject = subjectRepository.findSubjectById(subjectId);
        if(subject == null)
            throw  new MyException("专题不存在！");
        return subject;
    }

    private Assignment testAssignment(Integer assignmentId) throws MyException {
        Assignment assignment = assignmentRepository.findAssignmentById(assignmentId);
        if(assignment == null)
            throw  new MyException("词条任务不存在");
        return assignment;
    }

    private Task testTask(Integer taskId) throws  MyException {
        Task task = taskRepository.findTaskById(taskId);
        if(task == null)
            throw new MyException("任务不存在");
        return task;
    }

    private GroupMember testOrdinaryUser(User user, Subject subject) throws MyException {
        GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(user.getId(),subject.getId());
        if(groupMember == null)
            throw new MyException("用户未加入该专题！");
        return groupMember;
    }

    private GroupMember testSubjectMaker(User user, Subject subject) throws MyException {
        GroupMember groupMember = groupMemberRepository.findByUser_IdAndSubject_Id(user.getId(),subject.getId());
        if(groupMember == null || groupMember.getIdentity() != GroupMember.SUBJECTMAKER)
            throw new MyException("用户不是该专题的管理人！");
        return  groupMember;
    }

}
