package com.entry.entity.mysql.pk;

import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.Subject;
import com.entry.entity.mysql.User;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class TaskPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private GroupMemberPK groupMember;

    @OneToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    public TaskPK() {

    }

    public TaskPK(GroupMemberPK subjectGroupMember, Assignment assignment) {
        this.groupMember = subjectGroupMember;
        this.assignment = assignment;
    }

    public TaskPK(Subject subject, User user, Assignment assignment) {
        this.groupMember = new GroupMemberPK(subject, user);
        this.assignment = assignment;
    }

    @Transient
    public User getUser() {
        return this.groupMember.getUser();
    }

    @Transient
    public Subject getSubject() {
        return this.groupMember.getSubject();
    }

    public Assignment getAssignment() {
        return assignment;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.groupMember == null) ? 0 : this.groupMember.hashCode());
        result = PRIME * result + ((this.assignment == null) ? 0 : this.assignment.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }

        final TaskPK other = (TaskPK)obj;
        if(groupMember == null){
            if(other.groupMember != null){
                return false;
            }
        }else if(!groupMember.equals(other.groupMember)){
            return false;
        }
        if(assignment == null){
            if(other.assignment != null){
                return false;
            }
        }else if(!assignment.equals(other.assignment)){
            return false;
        }
        return true;
    }

}

