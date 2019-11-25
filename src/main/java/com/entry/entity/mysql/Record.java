package com.entry.entity.mysql;

import com.alibaba.fastjson.JSONArray;
import com.entry.entity.mysql.pk.Entry;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "record")
public class Record extends Entry {

    public final static Integer DRAWED = 3;     // 待提交
    public final static Integer TOAUDITED = 4;  // 待审核
    public final static Integer PASS = 5;       // 审核通过
    public final static Integer UNPASS = 6;     // 审核未通过
    public final static Integer OUTOFDATE = 7;   // 过期

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TINYINT default 3", nullable = false)
    private Integer state;

    @Column
    private String admitReason;

    @Column
    private String modifyReason;

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp saveTime;

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp judgeTime;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Record() {

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

    public String getAdmitReason() {
        return admitReason;
    }

    public void setAdmitReason(String admitReason) {
        this.admitReason = admitReason;
    }

    public JSONArray getModifyReason() {
        return JSONArray.parseArray(this.modifyReason);
    }

    public void setModifyReason(JSONArray reason) {
        this.modifyReason = reason.toJSONString();
    }

    public Long getSaveTime() {
        return saveTime.getTime();
    }

    public void setSaveTime(Long saveTime) {
        this.saveTime = new Timestamp(saveTime);
    }

    public Long getJudgeTime() {
        return judgeTime.getTime();
    }

    public void setJudgeTime(Long judgeTime) {
        this.judgeTime = new Timestamp(judgeTime);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}