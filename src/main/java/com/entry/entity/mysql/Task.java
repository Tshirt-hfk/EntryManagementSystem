package com.entry.entity.mysql;

import javax.persistence.*;
import java.sql.Timestamp;
import com.entry.entity.TaskPK;
import com.entry.entity.mysql.GroupMember;

@Entity
@Table(name = "task")
public class Task {

    @EmbeddedId
    private TaskPK pk;

    @Column(columnDefinition = "TINYINT default 0", nullable = false)
    private Integer state;


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
