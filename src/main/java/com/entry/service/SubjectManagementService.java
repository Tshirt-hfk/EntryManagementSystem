package com.entry.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.GroupMember;
import com.entry.entity.mysql.Subject;
import com.entry.entity.mysql.User;
import com.entry.entity.mysql.Task;
import com.entry.exception.MyException;

import java.util.List;

public interface SubjectManagementService {

    public void createSubject(Integer uesrId, String subjectName, String imageUrl, JSONArray field, Boolean isPublic, String intro, String goal, Long deadline) throws MyException;
    public void initSubjectAssignment(Integer subjectId, JSONArray entries, JSONArray relations) throws MyException;


    public void createAssignment(Integer userId, Integer subjectId, String EntryName, JSONArray field) throws MyException;
    public void publishAssignment(Integer userId, Integer subjectId, String reason, Integer deadline, List<Integer> entryIds) throws MyException;
    public void drawAssignment(Integer userId, Integer assignmentId) throws MyException;
    public void saveTask(Integer userId, Integer taskId, String entryName, String imageUrl, JSONArray field, String intro, JSONArray infoBox, String content, JSONArray reference) throws MyException;
    public void submitTask(Integer userId, Integer taskId, String reason) throws MyException;
    public void auditTask(Integer userId, Integer taskId, Boolean pass, String reason) throws MyException;

}

