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

    @Column
    private Integer originalId;

    @Column(columnDefinition = "varchar(50)")
    private String entryName;

    @Column(columnDefinition = "text")
    private String content;

    @Column
    private String modifyReason;

    @Column(nullable = false)
    private String field;

    @Column(columnDefinition = "TINYINT")
    private Integer state; //1:未发布；2：已发布，3：被领取

    @Column(columnDefinition = "INT default 864000000")
    private Integer deadline;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy="assignment", fetch=FetchType.LAZY)
    private List<Task> tasks;

    public Assignment() {

    }

    public Assignment(String entryName, String content, String field, Integer state, Subject subject) {
        this.entryName = entryName;
        this.content = content;
        this.field = field;
        this.state = state;
        this.subject = subject;
    }

    public Integer getId() {
        return id;
    }

    public String getEntryName() {
        return this.entryName;
    }

    public String getModifyReason(){ return this.modifyReason;}

    public void setModifyReason(String modifyReason){ this.modifyReason = modifyReason;}

    public String getContent() {
        return content;
    }

    public String getField() {
        return field;
    }

    public Subject getSubject() {
        return subject;
    }

    public List<Task> getTask() {
        return tasks;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state){
        this.state = state;
    }

    public Integer getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setField(String field) {
        this.field = field;
    }

}