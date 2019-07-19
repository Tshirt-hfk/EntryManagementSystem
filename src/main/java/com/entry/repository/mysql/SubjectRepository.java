package com.entry.repository.mysql;

import com.entry.entity.mysql.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query(value = "select * from subject where id =?1", nativeQuery = true)
    Subject findSubjectById(Integer id);

    @Query(value = "select * from subject where name like '%不知道啊%'", nativeQuery = true)
    List<Subject> findSubjectByKey(String keyword);
}
