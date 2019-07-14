package com.entry.repository.mysql;

import com.entry.entity.mysql.Assignment;
import com.entry.entity.mysql.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query(value = "select * from assignment where id = ?1", nativeQuery = true)
    Assignment findAssignmentById(Integer id);

    @Query(value = "delete from assignment where id = ?2", nativeQuery = true)
    @Modifying
    Integer deleteStateById(Integer state, Integer id);

    @Query(value = "select * from assignment where subject_id = ?1 and state = ?2", nativeQuery = true)
    List<Assignment> findAllBySubjectAndState(Integer id, Integer state);
}
