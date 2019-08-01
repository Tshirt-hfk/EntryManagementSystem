package com.entry.entity.mysql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="assignment")
public class Assignment {

    public final static Integer UNPUBLISHED = 1;// 未发布
    public final static Integer PUBLISHED = 2;  // 已发布
    public final static Integer DRAWED = 3;     // 待提交
    public final static Integer TOAUDITED = 4;  // 待审核
    public final static Integer TOSUBMIT = 5;   // 待提交
    public final static Integer SUBMITED = 6;   // 待提交

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "INT default -1", nullable = false)
    private Integer originalId;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String entryName;

    @Column(columnDefinition ="varchar(255)", nullable = false)
    private String field;

    @Column(columnDefinition = "TEXT")
    private String intro;

    @Column
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String infoBox;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TINYINT default 1", nullable = false)
    private Integer state; //1:未发布；2：已发布，3：被领取

    @Column
    private Integer deadline;

    @Column
    private String modifyReason;

    @Column(columnDefinition = "TEXT")
    private String relation;

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
        this.field = field.toJSONString();
        this.intro = "";
        this.imageUrl = "";
        this.infoBox = (new JSONArray()).toJSONString();
        this.content = "";
        this.relation = (new JSONArray()).toJSONString();
        this.state = 1;
        this.subject = subject;

    }

    public Assignment(Integer originalId, String entryName, String field, String intro, String imageUrl, String infoBox, String content, Subject subject) {
        this.originalId = originalId;
        this.entryName = entryName;
        this.field = field;
        this.intro = intro;
        this.imageUrl = imageUrl;
        this.infoBox = infoBox;
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

    public Integer getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public JSONArray getField() {
        return JSONArray.parseArray(field);
    }

    public void setField(JSONArray field) {
        this.field = field.toJSONString();
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public JSONArray getInfoBox() {
        return JSONArray.parseArray(infoBox);
    }

    public void setInfoBox(JSONArray infoBox) {
        this.infoBox = infoBox.toJSONString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public JSONArray getRelation() {
        return JSONArray.parseArray(this.relation);
    }

    public void setRelation(JSONArray relation) {
        this.relation = relation.toJSONString();
    }


}