package com.entry.repository.mysql;

import com.entry.entity.mysql.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {

    @Query(value = "select * from record where id = ?1", nativeQuery = true)
    Record findRecordById(Integer id);

    @Query(value = "select * from record where user_id = ?1 and state = ?2",nativeQuery = true)
    List<Record> findAllByUser_IdAndState(Integer userId, Integer state);

    @Query(value = "select * from record where state = ?1",nativeQuery = true)
    List<Record> findAllByState(Integer state);

    @Query(value = "select * from record where original_id= ?1 and user_id = ?2",nativeQuery = true)
    Record findByOriginalIdAndUser(Integer originalId, Integer userId);

    @Query(value = "select * from record where original_id= ?1 and user_id = ?2 and state<=4",nativeQuery = true)
    Record findByOriginalIdAndUserAndState(Integer originalId, Integer userId);

}