package com.entry.repository.mysql;

import com.entry.entity.mysql.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "select * from task where Assignment_id=?1 and user_id=?2", nativeQuery = true)
    Task findTaskByAssignment_IdAAndUser_Id(Integer assignmet_id,Integer user_id);

    @Query(value = "select * from task where user_id = ?1", nativeQuery = true)
    List<Task> findTaskByUserId(Integer id);

    @Query(value = "select * from task where subject_id = ?1 and user_id = ?2 and state = ?3", nativeQuery = true)
    List<Task> findAllBySubject_IdAndUser_IdAAndState(Integer subjectId, Integer userId, Integer state);

    @Query(value = "update task set state = ?1 where assignment_id = ?2", nativeQuery = true)
    @Modifying
    Integer updateStateByAssignmentId(Integer state, Integer assignment_id);

    @Query(value = "delete from task where assignment_id = ?2", nativeQuery = true)
    @Modifying
    Integer deleteStateByAssignmentId(Integer state, Integer assignment_id);
}