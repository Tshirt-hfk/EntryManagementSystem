package com.entry.entity.mysql;

import com.entry.entity.GroupMemberPK;

import javax.persistence.*;

@Entity(name = "groupmember")
public class GroupMember {

    @EmbeddedId
    private GroupMemberPK pk;

    @Column(name = "identity",columnDefinition = "TINYINT default 0")
    private Integer identity;  // 0：普通用户；1：专题创建人，可以审核词条

    public GroupMember() {

    }

    public GroupMember(GroupMemberPK pk, Integer identity) {
        this.pk = pk;
        this.identity = identity;
    }

    public GroupMember(Subject subject, User user, Integer identity) {
        this.pk = new GroupMemberPK(subject, user);
        this.identity = identity;
    }

    @Transient
    public User getUser() {
        return this.pk.getUser();
    }
    
    @Transient
    public Subject getSubject() {
        return  this.pk.getSubject();
    }

    public Integer getIdentity() {
        return  this.identity;
    }
}
