package com.entry.repository.mysql;

import com.entry.entity.mysql.GroupMember;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Integer> {

    @Query(value = "select * from groupmember where user_id=?1 and identity=?2", nativeQuery = true)
    List<GroupMember> findAllByUser_IdAndIdentity(Integer userId, Integer identity);

    @Query(value = "select * from groupmember where user_id=?1 and subject_id=?2", nativeQuery = true)
    GroupMember findByUser_IdAndSubject_Id(Integer userId, Integer subjectId);

    @Query(value = "select * from groupmember where subject_id=?1 and identity=?2", nativeQuery = true)
    List<GroupMember> findAllBySubject_IdAndIdentity(Integer subjectId, Integer identity);
}
