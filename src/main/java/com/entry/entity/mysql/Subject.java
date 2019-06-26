package com.entry.entity.mysql;

import org.neo4j.ogm.annotation.NodeEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增主键
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String introduction;

    @Column(nullable = false)
    private String field;

    @OneToMany(mappedBy = "subject",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Assignment> assignmentlist;

    public List<SubjectGroup> getSubjectGroupList() {
        return subjectGroupList;
    }

    public void setSubjectGroupList(List<SubjectGroup> subjectGroupList) {
        this.subjectGroupList = subjectGroupList;
    }

    @OneToMany(mappedBy="subject",cascade=CascadeType.ALL)
    private List<SubjectGroup> subjectGroupList;

    public List<Assignment> getAssignmentlist() {
        return assignmentlist;
    }

    public void setAssignmentlist(List<Assignment> assignmentlist) {
        this.assignmentlist = assignmentlist;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
