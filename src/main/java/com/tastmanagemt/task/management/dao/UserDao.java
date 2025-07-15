package com.tastmanagemt.task.management.dao;

import com.tastmanagemt.task.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    List<User> findByFirstNameContainingIgnoreCaseOrderByFirstNameAsc(String firstName);

    boolean existsByEmail(String email);
    boolean existsByTelephoneNumber(String telephoneNumber);

}