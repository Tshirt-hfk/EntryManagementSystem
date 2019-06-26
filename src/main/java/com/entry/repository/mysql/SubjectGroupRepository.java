package com.entry.repository.mysql;

import com.entry.entity.mysql.SubjectGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectGroupRepository extends JpaRepository<SubjectGroup,Integer> {

    @Query(value = "select * from SubjectGroup where id = ?1", nativeQuery = true)
    SubjectGroup findSubjectGroupById(Integer id);

}
