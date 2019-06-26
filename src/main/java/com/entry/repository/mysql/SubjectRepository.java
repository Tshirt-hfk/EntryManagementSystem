package com.entry.repository.mysql;

import com.entry.entity.mysql.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    @Query(value = "select * from subject where id =?1", nativeQuery = true)
    Subject findSubjectById(Integer id);

}
