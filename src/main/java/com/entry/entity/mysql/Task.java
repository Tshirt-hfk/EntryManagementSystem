package com.entry.entity.mysql;

import javax.persistence.*;
import java.sql.Timestamp;
import com.entry.entity.mysql.pk.TaskPK;

@Entity
@Table(name = "task")
public class Task {

    public final static Integer UNPUBLISHED = 1;// 未发布
    public final static Integer PUBLISHED = 2;  // 已发布
    public final static Integer DRAWED = 3;     // 待提交
    public final static Integer TOAUDITED = 4;  // 待审核
    public final static Integer TOSUBMIT = 5;   // 审核通过

    @EmbeddedId
    private TaskPK pk;

    @Column(columnDefinition = "TINYINT default 0", nullable = false)
    private Integer state;  // 4：待提交, 5：待审核

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

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp deadline;

    public Task() {

    }

    public Task(TaskPK pk, String entryName, Integer state, String content, Timestamp deadline) {
        this.pk = pk;
        this.entryName = entryName;
        this.state = state;
        this.content = content;
        this.deadline = deadline;
    }

    public Task(GroupMember groupMember,String entryName, Assignment assignment, Integer state, String content, Timestamp deadline) {
        this.pk = new TaskPK(groupMember.getSubject(), groupMember.getUser(), assignment);
        this.entryName = entryName;
        this.state = state;
        this.content = content;
        this.deadline = deadline;
    }

    public Task(Subject subject,String entryName, User user, Assignment assignment, Integer state, String content, Timestamp deadline) {
        this.pk = new TaskPK(subject, user, assignment);
        this.entryName = entryName;
        this.state = state;
        this.content = content;
        this.deadline = deadline;
    }

    @Transient
    public Subject getSubject() {
        return this.pk.getSubject();
    }

    @Transient
    public User getUser() {
        return this.pk.getUser();
    }

    @Transient
    public Assignment getAssignment() {
        return  this.pk.getAssignment();
    }

    public TaskPK getPk() {
        return pk;
    }

    public void setPk(TaskPK pk) {
        this.pk = pk;
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
