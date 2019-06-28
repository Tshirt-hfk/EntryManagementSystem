package com.entry.repository.mysql;

import com.entry.entity.mysql.GroupMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Integer> {

    @Query(value = "select * from groupmember where id = ?1", nativeQuery = true)
    GroupMember findGroupMemberById(Integer id);

}
