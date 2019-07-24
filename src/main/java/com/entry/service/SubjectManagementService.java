package com.entry.service;


import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.GroupMember;
import com.entry.entity.mysql.Subject;
import com.entry.entity.mysql.User;
import com.entry.entity.mysql.Task;
import com.entry.exception.MyException;

import java.util.List;

public interface SubjectManagementService {

    public void createAssignment(User user, Subject subject, String Entryname, List<String> fieldList) throws MyException;
    public void publishAssignment(User user, Subject subject, String reason, Integer deadline, List<Integer> entryIds) throws MyException;
    public void drawAssignment(User user, Subject subject, Assignment assignment) throws MyException;
    public void saveTask(User user, Subject subject, Task task, String content) throws MyException;
    public void submitTask(User user, Subject subject, Task task, String content, String reason) throws MyException;
    public void auditTask(User user, Subject subject, Task task, Boolean pass, String reason) throws MyException;

}

