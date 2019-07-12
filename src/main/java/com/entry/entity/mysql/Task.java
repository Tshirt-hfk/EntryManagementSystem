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
    public final static Integer TOSUBMIT = 5;   // 待提交

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

    @Column(columnDefinition = "varchar(255)")
    private String field;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp deadline;

    public Task() {

    }

    public Task(TaskPK pk, Integer state, String content, Timestamp deadline) {
        this.pk = pk;
        this.state = state;
        this.content = content;
        this.deadline = deadline;
    }

    public Task(GroupMember groupMember, Assignment assignment, Integer state, String content, Timestamp deadline) {
        this.pk = new TaskPK(groupMember.getSubject(), groupMember.getUser(), assignment);
        this.state = state;
        this.content = content;
        this.deadline = deadline;
    }

    public Task(Subject subject, User user, Assignment assignment, Integer state, String content, Timestamp deadline) {
        this.pk = new TaskPK(subject, user, assignment);
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

    public Integer getState() {
        return state;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getDeadline() {
        return deadline;
    }
}
