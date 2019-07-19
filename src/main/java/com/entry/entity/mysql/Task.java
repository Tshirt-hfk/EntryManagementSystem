package com.entry.entity.mysql;

import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "task")
public class Task {

    public final static Integer DRAWED = 3;     // 待提交
    public final static Integer TOAUDITED = 4;  // 待审核
    public final static Integer PASS = 5;       // 审核通过
    public final static Integer UNPASS = 6;     // 审核未通过

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TINYINT default 0", nullable = false)
    private Integer state; // 待提交  // 4：待提交, 5：待审核

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Column(columnDefinition = "varchar(50)")
    private String entryName;

    @Column(columnDefinition = "varchar(255)")
    private String field;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String admitReason;

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp deadline;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task() {

    }

    public Task(Subject subject, User user, Assignment assignment,String entryName, Integer state, String content, Timestamp deadline) {
        this.subject = subject;
        this.user = user;
        this.assignment =assignment;
        this.entryName = entryName;
        this.state = state;
        this.content = content;
        this.deadline = deadline;
    }

    public Task(Subject subject,User user, Assignment assignment, Integer state){
        this.subject = subject;
        this.user = user;
        this.assignment =assignment;
        this.entryName = assignment.getEntryName();
        this.field = assignment.getField();
        this.state = state;
        this.content = assignment.getContent();
        this.deadline = new Timestamp((new Date()).getTime()+assignment.getDeadline());
    }

    public Integer getId() {
        return id;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public User getUser() {
        return this.user;
    }

    public Assignment getAssignment() {
        return  this.assignment;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDeadline() {
        return deadline.getTime();
    }

    public void setDeadline(Long deadline) {
        this.deadline = new Timestamp(deadline);
    }
}
