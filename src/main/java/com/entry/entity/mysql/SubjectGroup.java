package com.entry.entity.mysql;

import javax.persistence.*;

@Entity(name = "subjectgroup")
public class SubjectGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "subject_id", unique = true)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "identity",columnDefinition = "TINYINT default 0")
    private Integer identity;  // 0：普通用户；1：专题创建人，可以审核词条

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }


}
