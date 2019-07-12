package com.entry.repository.mysql;

import com.entry.entity.mysql.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "select * from task where id = ?1", nativeQuery = true)
    Task findTaskById(Integer id);

    @Query(value = "select * from task where Assignment_id=?1 and user_id=?2", nativeQuery = true)
    Task findTaskByAssignment_IdAAndUser_Id(Integer assignmet_id,Integer user_id);

    @Query(value = "select * from task where user_id = ?1", nativeQuery = true)
    public List<Task> findTaskByUserId(Integer id);
}
