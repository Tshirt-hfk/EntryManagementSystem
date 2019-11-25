package com.entry.repository.mysql;

import com.entry.entity.mysql.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query(value = "select * from subject where id =?1", nativeQuery = true)
    Subject findSubjectById(Integer id);

    @Query(value = "select * from subject where name like concat('%',?1,'%')", nativeQuery = true)
    List<Subject> findSubjectByKey(String keyword);

    @Query(value = "SELECT * FROM subject where is_public = 1 ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<Subject> findSubjectByRand(Integer number);

    @Query(value = "SELECT * FROM subject where field = ?1", nativeQuery = true)
    List<Subject> findSubjectsByField(String field);
}
