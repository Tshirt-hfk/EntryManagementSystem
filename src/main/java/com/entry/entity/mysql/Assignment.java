package com.entry.entity.mysql;

import com.alibaba.fastjson.JSONArray;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="assignment")
public class Assignment extends Entry {

    public final static Integer UNPUBLISHED = 1;// 未发布
    public final static Integer PUBLISHED = 2;  // 已发布
    public final static Integer DRAWED = 3;     // 待提交
    public final static Integer TOAUDITED = 4;  // 待审核
    public final static Integer TOSUBMIT = 5;   // 待提交
    public final static Integer SUBMITED = 6;   // 待提交

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TINYINT default 1", nullable = false)
    private Integer state; //1:未发布；2：已发布，3：被领取

    @Column
    private Integer deadline;

    @Column
    private String modifyReason;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy="assignment", fetch=FetchType.LAZY)
    private List<Task> tasks;

    public Assignment() {

    }

    public Assignment(String entryName, JSONArray field, Subject subject) {
        this.originalId = -1;
        this.entryName = entryName;
        if(field!=null)
            this.field = field.toJSONString();
        else
            this.field = (new JSONArray()).toJSONString();
        this.intro = "";
        this.imageUrl = "";
        this.infoBox = (new JSONArray()).toJSONString();
        this.content = "";
        this.relation = (new JSONArray()).toJSONString();
        this.state = 1;
        this.subject = subject;

    }

    public Assignment(Integer originalId, String entryName, JSONArray field, String intro, String imageUrl, JSONArray infoBox, String content, Subject subject) {
        this.originalId = originalId;
        this.entryName = entryName;
        if(field!=null)
            this.field = field.toJSONString();
        else
            this.field = (new JSONArray()).toJSONString();
        this.intro = intro;
        this.imageUrl = imageUrl;
        if(infoBox!=null)
            this.infoBox = infoBox.toJSONString();
        else
            this.infoBox = (new JSONArray()).toJSONString();
        this.content = content;
        this.relation = (new JSONArray()).toJSONString();
        this.state = 1;
        this.subject = subject;
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

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public JSONArray getModifyReason() {
        return JSONArray.parseArray(this.modifyReason);
    }

    public void setModifyReason(JSONArray reason) {
        this.modifyReason = reason.toJSONString();
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}