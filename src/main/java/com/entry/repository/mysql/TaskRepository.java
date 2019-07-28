package com.entry.repository.mysql;

import com.entry.entity.mysql.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "select * from task where id = ?1", nativeQuery = true)
    Task findTaskById(Integer id);

    @Query(value = "select * from task where Assignment_id=?1 and user_id=?2", nativeQuery = true)
    Task findTaskByAssignment_IdAAndUser_Id(Integer assignmentId,Integer useId);


    @Query(value = "select * from task where subject_id = ?1 and user_id = ?2 and state = ?3", nativeQuery = true)
    List<Task> findAllBySubject_IdAndUser_IdAndState(Integer subjectId, Integer userId, Integer state);

    @Query(value = "select * from task where subject_id =?1 and state = ?2",nativeQuery = true)
    List<Task> findAllBySubject_IdAndState(Integer subjectId, Integer state);

    @Query(value = "select * from task where user_id = ?1 and state = ?2",nativeQuery = true)
    List<Task> findAllByUser_IdAndState(Integer userId, Integer state);

}