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

    @Query(value = "select * from assignment where subject_id = ?1 and state = ?2", nativeQuery = true)
    List<Assignment> findAllBySubject_IdAndState(Integer id, Integer state);

    @Query(value = "SELECT * FROM assignment where state = 2 ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<Assignment> getRecommendAssignmentByRand(Integer number);

    @Query(value = "select * from assignment where entry_name like concat('%',?1,'%')", nativeQuery = true)
    List<Assignment> findAssignmentByKey(String keyword);
}
