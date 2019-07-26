package com.entry.entity.mysql;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "INT default -1", nullable = false)
    private Integer originalId;

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String entryName;

    @Column(columnDefinition ="varchar(255)", nullable = false)
    private String field;

    @Column
    private String intro;

    @Column
    private String imageUrl;

    @Column
    private String infoBox;

    @Column
    private String content;

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

    public Assignment(String entryName, String field, Subject subject) {
        this.originalId = -1;
        this.entryName = entryName;
        this.field = field;
        this.intro = "";
        this.imageUrl = "";
        this.infoBox = "";
        this.content = "";
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
        this.state = 0;
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

    public String getInfoBox() {
        return infoBox;
    }

    public void setInfoBox(String infoBox) {
        this.infoBox = infoBox;
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

    public String getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(String modifyReason) {
        this.modifyReason = modifyReason;
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