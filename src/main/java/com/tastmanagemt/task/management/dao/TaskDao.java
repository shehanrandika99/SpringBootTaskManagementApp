package com.tastmanagemt.task.management.dao;

import com.tastmanagemt.task.management.model.Task;
import com.tastmanagemt.task.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskDao extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Task.TaskStatus status);
    List<Task> findByAssignedUser(User user);
    List<Task> findByAssignedUserAndStatus(User user, Task.TaskStatus status);
}
