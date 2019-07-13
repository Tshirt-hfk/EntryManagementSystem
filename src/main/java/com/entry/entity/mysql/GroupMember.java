package com.entry.entity.mysql;

import com.entry.entity.mysql.pk.GroupMemberPK;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity(name = "groupmember")
public class GroupMember {

    public final static Integer ORDINRYUSER = 1;
    public final static Integer SUBJECTMAKER = 2;

    @EmbeddedId
    private GroupMemberPK pk;

    @Column(name = "identity",columnDefinition = "TINYINT default 0")
    private Integer identity;  // 1：普通用户；2：专题创建人，可以审核词条

    @Column(columnDefinition = "INT default 0")
    private Integer myCompletedCount;

    public GroupMember() {

    }

    public GroupMember(GroupMemberPK pk, Integer identity) {
        this.pk = pk;
        this.identity = identity;
    }

    public GroupMember(Subject subject, User user, Integer identity) {
        this.pk = new GroupMemberPK(subject, user);
        this.identity = identity;
        this.myCompletedCount = 0;
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

    public Integer getMyCompletedCount() {
        return myCompletedCount;
    }

    public void setMyCompletedCount(Integer myCompletedCount) {
        this.myCompletedCount = myCompletedCount;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

}
