package com.entry.entity.mysql;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增主键
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String introduction;

    @Column(nullable = false)
    private String field;

    @OneToMany(mappedBy = "subject",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Assignment> assignmentlist;

    @OneToMany(mappedBy="pk.groupMember.subject",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Task> taskList;

    @OneToMany(mappedBy="pk.subject",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<GroupMember> groupMemberList;

    public Subject() {

    }

    public Subject(String name, String introduction, String field) {
        this.name = name;
        this.introduction = introduction;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getField() {
        return field;
    }

    public List<Assignment> getAssignmentlist() {
        return assignmentlist;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public List<GroupMember> getGroupMemberList() {
        return groupMemberList;
    }


}
