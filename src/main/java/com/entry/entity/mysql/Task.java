package com.entry.entity.mysql;

import com.entry.entity.mysql.pk.Entry;

import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "task")
public class Task extends Entry {

    public final static Integer DRAWED = 3;     // 待提交
    public final static Integer TOAUDITED = 4;  // 待审核
    public final static Integer PASS = 5;       // 审核通过
    public final static Integer UNPASS = 6;     // 审核未通过
    public final static Integer OUTOFDATE = 7;   // 过期

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TINYINT default 3", nullable = false)
    private Integer state;

    @Column
    private String admitReason;

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp deadline;

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp saveTime;

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp judgeTime;

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

    public Task(Subject subject,User user, Assignment assignment){
        this.subject = subject;
        this.user = user;
        this.assignment =assignment;
        this.originalId = assignment.getOriginalId();
        this.entryName = assignment.getEntryName();
        this.imageUrl = assignment.getImageUrl();
        this.field = assignment.getField().toJSONString();
        this.intro = assignment.getIntro();
        this.infoBox = assignment.getInfoBox().toJSONString();
        this.content = assignment.getContent();
        this.relation = assignment.getRelation().toJSONString();
        this.state = Task.DRAWED;
        this.deadline = new Timestamp((new Date()).getTime()+assignment.getDeadline());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAdmitReason() {
        return admitReason;
    }

    public void setAdmitReason(String admitReason) {
        this.admitReason = admitReason;
    }

    public Long getDeadline() {
        return deadline.getTime();
    }

    public void setDeadline(Long deadline) {
        this.deadline = new Timestamp(deadline);
    }

    public Long getSaveTime() {
        return saveTime.getTime();
    }

    public void setSaveTime(Long saveTime) {
        this.saveTime = new Timestamp(saveTime);
    }

    public Long getJudgeTime() {
        return judgeTime.getTime();
    }

    public void setJudgeTime(Long judgeTime) {
        this.judgeTime = new Timestamp(judgeTime);
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}