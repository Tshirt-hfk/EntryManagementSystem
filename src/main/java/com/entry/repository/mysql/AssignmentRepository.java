package com.entry.repository.mysql;

import com.entry.entity.mysql.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query(value = "select * from assignment where id = ?1", nativeQuery = true)
    Assignment findAssignmentById(Integer id);
}
