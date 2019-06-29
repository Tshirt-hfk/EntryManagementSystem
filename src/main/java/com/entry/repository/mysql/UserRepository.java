package com.entry.repository.mysql;

import com.entry.entity.mysql.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from user where id = ?1", nativeQuery = true)
    User findUserById(Integer id);

    @Query(value = "select * from user where name = ?1", nativeQuery = true)
    User findUserByName(String name);

}
