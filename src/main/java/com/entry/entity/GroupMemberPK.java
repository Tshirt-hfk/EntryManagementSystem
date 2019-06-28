package com.entry.entity;


import com.entry.entity.mysql.Subject;
import com.entry.entity.mysql.User;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class GroupMemberPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public GroupMemberPK() {

    }

    public GroupMemberPK(Subject subject, User user) {
        this.subject = subject;
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.subject == null) ? 0 : this.subject.hashCode());
        result = PRIME * result + ((this.user == null) ? 0 : this.user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }

        final GroupMemberPK other = (GroupMemberPK)obj;
        if(subject == null){
            if(other.subject != null){
                return false;
            }
        }else if(!subject.equals(other.subject)){
            return false;
        }
        if(user == null){
            if(other.user != null){
                return false;
            }
        }else if(!user.equals(other.user)){
            return false;
        }
        return true;
    }

}
