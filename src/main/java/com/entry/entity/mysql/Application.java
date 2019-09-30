package com.entry.entity.mysql;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "application")
public class Application {

    // 0: 未审核;;;; 1: pass;;;;;; 2: unpass;;;;;
    public final static Integer UNADUIT = 0;
    public final static Integer PASS = 1;
    public final static Integer UNPASS = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增主键
    private Integer id;

    @Column(nullable = false, columnDefinition = "TINYINT default 1")
    private Integer affair;  // 1: 专题制作人权限申请;;;;;

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private Timestamp startTime;

    @Column(nullable = false, columnDefinition = "TINYINT default 1")
    private Integer state;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Application() {

    }

    public Application(Integer affair, User user){
        this.affair = affair;
        this.user = user;
        this.state = Application.UNADUIT;
        this.startTime = new Timestamp((new Date()).getTime());
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getAffair() {
        return this.affair;
    }

    public void setAffair(Integer affair) {
        this.affair = affair;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getStartTime() {
        return startTime.getTime();
    }

    public void setStartTime(Long startTime) {
        this.startTime = new Timestamp(startTime);
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
