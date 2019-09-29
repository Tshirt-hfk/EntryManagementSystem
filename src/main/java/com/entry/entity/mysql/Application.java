package com.entry.entity.mysql;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增主键
    private Integer id;

    @Column(nullable = false, columnDefinition = "TINYINT default 1")
    private Integer affair;  // 1: 专题制作人权限申请;;;;;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Application() {

    }

    public Application(Integer affair, User user){
        this.affair = affair;
        this.user = user;
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
}
