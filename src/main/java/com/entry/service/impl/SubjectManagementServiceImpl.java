package com.entry.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.entity.mysql.*;
import com.entry.entity.mysql.pk.GroupMemberPK;
import com.entry.exception.MyException;
import com.entry.repository.mysql.*;
import com.entry.service.SubjectManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("subjectManagementService")
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
     * 创建专题
     * @param userId
     * @param subjectName
     * @param imageUrl
     * @param field
     * @param isPublic
     * @param intro
     * @param goal
     * @param deadline
     * @throws MyException
     */
    @Override
    public Subject createSubject(Integer userId, String subjectName, String imageUrl, JSONArray field, Boolean isPublic, String intro, String goal, Long deadline) throws MyException {
        User user = this.testUser(userId);
        if(user.getAuthorith()!=User.SUBJECTMAKER)
            throw new MyException("用户不具有创建专题的权限！");
        Subject subject = new Subject(imageUrl,subjectName,user.getName(),intro,goal,field,new Timestamp(deadline),isPublic);
        GroupMember groupMember = new GroupMember(new GroupMemberPK(subject,user), GroupMember.SUBJECTMAKER);
        subjectRepository.save(subject);
        groupMemberRepository.save(groupMember);
        return subject;
    }

    /**
     * 初始化专题的任务词条
     * @param subjectId
     * @param entries
     * @param relations
     * @throws MyException
     */
    @Override
    public void initSubjectAssignment(Integer subjectId, JSONArray entries, JSONArray relations) throws MyException {
        System.out.println("initSubjectAssignment");
        Subject subject = this.testSubject(subjectId);
        subject.setInitState(true);
        List<Assignment> assignmentList = new ArrayList<>();
        int len1=entries.size();
        JSONObject tmp = new JSONObject();
        for (int i=0;i<len1;i++){
            JSONObject js = entries.getJSONObject(i);
            System.out.println(js.toJSONString());
            String entryName = js.getString("name");
            Integer originalId = Integer.parseInt(js.getString("id"));
            Assignment assignment;
            if(originalId<0){
                assignment = new Assignment(entryName,subject.getField(),subject);
            }else {
                JSONObject info = js.getJSONObject("info");
                assignment = new Assignment(originalId,entryName,js.getJSONArray("category"),info.getString("intro"),info.getString("imageUrl"),info.getJSONArray("infoBox"),info.getString("content"),subject);
            }
            assignmentList.add(assignment);
            tmp.put(js.getString("entryName"),new JSONArray());
        }
        int len2 = relations.size();
        for(int i=0;i<len2;i++){
            JSONObject js = relations.getJSONObject(i);
            String name1 = js.getString("node1");
            String name2 = js.getString("node2");
            String rel = js.getString("rel_type");
            if("co_occurrence".equals(rel)){
                if(tmp.getJSONArray(name1)!=null){
                    JSONObject r = new JSONObject();
                    r.put("entry",name2);
                    r.put("type",1);
                    tmp.getJSONArray(name1).add(r);
                }
                if(tmp.getJSONArray(name2)!=null){
                    JSONObject r = new JSONObject();
                    r.put("entry",name1);
                    r.put("type","贡献词");
                    tmp.getJSONArray(name2).add(r);
                }
            }else if("is-a".equals(rel)){
                if(tmp.getJSONArray(name1)!=null){
                    JSONObject r = new JSONObject();
                    r.put("entry",name2);
                    r.put("type","上位词");
                    tmp.getJSONArray(name1).add(r);
                }
                if(tmp.getJSONArray(name2)!=null){
                    JSONObject r = new JSONObject();
                    r.put("entry",name1);
                    r.put("type","下位词");
                    tmp.getJSONArray(name2).add(r);
                }
            }

        }
        for(int i=0;i<assignmentList.size();i++){
            if(tmp.getJSONArray(assignmentList.get(i).getEntryName())!=null){
                assignmentList.get(i).setRelation(tmp.getJSONArray(assignmentList.get(i).getEntryName()));
            }
            assignmentRepository.save(assignmentList.get(i));
        }
        subjectRepository.save(subject);
    }



    /**
     * 创建词条任务
     * @param userId
     * @param subjectId
     * @param EntryName
     * @param field
     * @throws MyException
     */
    @Override
    public Assignment createAssignment(Integer userId, Integer subjectId, String EntryName, JSONArray field) throws MyException {
        // TODO 判断该词条是否以及在数据库中
        User user = this.testUser(userId);
        Subject subject = this.testSubject(subjectId);
        this.testSubjectMaker(user,subject);
        Assignment assignment = new Assignment(EntryName,field,subject);
        assignmentRepository.save(assignment);
        return assignment;
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
    public void publishAssignment(Integer userId, Integer subjectId, JSONArray reason, Integer deadline, List<Integer> entryIds) throws MyException  {
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
    public void saveTask(Integer userId, Integer taskId, String entryName, String imageUrl, JSONArray field, String intro, JSONArray infoBox, String content, JSONArray reference, JSONArray rel) throws MyException {
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
        task.setRelation(rel);
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
            assignment.setField(task.getField());
            assignment.setImageUrl(task.getImageUrl());
            assignment.setIntro(task.getIntro());
            assignment.setInfoBox(task.getInfoBox());
            assignment.setContent(task.getContent());
            assignment.setState(Assignment.TOSUBMIT);
            assignment.setRelation(task.getRelation());
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

    @Override
    public Assignment submitEntry(Integer userId, Integer assignmentId) throws  MyException {
        User user = this.testUser(userId);
        Assignment assignment = this.testAssignment(assignmentId);
        this.testSubjectMaker(user,assignment.getSubject());
        if(assignment.getState()!=Assignment.TOSUBMIT)
            throw new MyException("任务未完成审核！");
        assignment.setState(Assignment.SUBMITED);
        assignmentRepository.save(assignment);
        return assignment;
    }

    /**
     * 获取任务内容
     * @param userId
     * @param assignmentId
     * @return
     * @throws MyException
     */
    @Override
    public JSONObject getAssignment(Integer userId, Integer assignmentId) throws  MyException {
        User user = this.testUser(userId);
        Assignment assignment = this.testAssignment(assignmentId);
        this.testOrdinaryUser(user,assignment.getSubject());
        JSONObject result = new JSONObject();
        result.put("entryName",assignment.getEntryName());
        result.put("imageUrl",assignment.getImageUrl());
        result.put("intro",assignment.getIntro());
        result.put("field",assignment.getField());
        result.put("infoBox", assignment.getInfoBox());
        result.put("content", assignment.getContent());
        result.put("relation", assignment.getRelation());
        return result;
    }

    /**
     * 获取领取的任务内容
     * @param userId
     * @param taskId
     * @return
     * @throws MyException
     */
    @Override
    public JSONObject getTask(Integer userId, Integer taskId) throws  MyException {
        User user = this.testUser(userId);
        Task task = this.testTask(taskId);
        if(userId!=task.getUser().getId())
            this.testSubjectMaker(user,task.getSubject());
        this.testTaskOutOfDate(task);
        JSONObject result = new JSONObject();
        result.put("entryName",task.getEntryName());
        result.put("imageUrl",task.getImageUrl());
        result.put("intro",task.getIntro());
        result.put("field",task.getField());
        result.put("infoBox", task.getInfoBox());
        result.put("content", task.getContent());
        result.put("relation", task.getRelation());
        return result;
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
            throw new MyException("用户不是该专题的管理者！");
        return  groupMember;
    }

}
