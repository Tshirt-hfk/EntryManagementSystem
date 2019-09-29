package com.entry.repository.mysql;

import com.entry.entity.mysql.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query(value = "select * from application where id = ?1", nativeQuery = true)
    Application findApplicationById(Integer id);

    @Query(value = "select * from application where affair = ?1 and state == ?2",nativeQuery = true)
    List<Application> findAllByAffairAndState(Integer affair, Integer state);


}
