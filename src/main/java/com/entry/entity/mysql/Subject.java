package com.entry.entity.mysql;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "subject")
public class Subject {

    public Integer getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增主键
    private Integer id;

    @Column(columnDefinition = "varchar(255)")
    private String imageUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(50)")
    private String creator;  // 创建者姓名

    @Column(nullable = false, columnDefinition = "INT default 0")
    private Integer currentCount;

    @Column(nullable = false, columnDefinition = "INT default 0")
    private Integer totalCount;

    @Column(columnDefinition = "text")
    private String introduction;

    @Column(columnDefinition = "text")
    private String goal;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private Timestamp deadline;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean isPublic;

    @OneToMany(mappedBy = "subject",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Assignment> assignmentlist;

    @OneToMany(mappedBy="pk.groupMember.subject",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Task> taskList;

    @OneToMany(mappedBy="pk.subject",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<GroupMember> groupMemberList;

    public Subject() {

    }

    public Subject(String imageUrl,String  name,String creator, String introduction, String goal, String field,Timestamp deadline, boolean isPublic) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.creator = creator;
        this.introduction = introduction;
        this.goal = goal;
        this.field = field;
        this.deadline = deadline;
        this.isPublic = isPublic;
        this.currentCount = 0;
        this.totalCount = 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadLine) {
        this.deadline = deadLine;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public List<Assignment> getAssignmentlist() {
        return assignmentlist;
    }

    public void setAssignmentlist(List<Assignment> assignmentlist) {
        this.assignmentlist = assignmentlist;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public List<GroupMember> getGroupMemberList() {
        return groupMemberList;
    }

    public void setGroupMemberList(List<GroupMember> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }
}
