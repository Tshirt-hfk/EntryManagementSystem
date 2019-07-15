package com.entry.entity.mysql;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="assignment")
public class Assignment {

    public final static Integer UNPUBLISH = 1;      // 未发布
    public final static Integer PUBLISH = 2;        // 已发布
    public final static Integer TOSUBMIT = 3;       // 待提交
    public final static Integer UNPASS = 4;         // 未通过
    public final static Integer TOAUDITED = 5;      // 待审核

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *  词条管理数据库的词条id
     */
    @Column
    private Integer originalId;

    /**
     * 词条名称
     */
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String entryName;

    /**
     * 词条内容
     */
    @Column(columnDefinition = "text default ''")
    private String content;

    /**
     * 词条领域
     */
    @Column(nullable = false)
    private String field;

    /**
     * 状态
     */
    @Column(columnDefinition = "TINYINT")
    private Integer state;

    /**
     * 期限 专题制作人设置
     */
    @Column(columnDefinition = "INT default 86400000")
    private Integer timeLimit;

    /**
     * 截至日期 任务被领取时设置
     */
    @Column
    private Timestamp deadline;

    /**
     * 外键，所属专题
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    /**
     * 外键，任务领取人
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Assignment() {

    }

    public Assignment(Integer originalId,String entryName,String content,String field,Integer state,Integer timeLimit,Long deadline,Subject subject,User user){

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Long getDeadline() {
        return deadline.getTime();
    }

    public void setDeadline(Long deadline) {
        this.deadline = new Timestamp(deadline);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
