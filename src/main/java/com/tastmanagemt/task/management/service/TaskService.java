package com.tastmanagemt.task.management.service;

import com.tastmanagemt.task.management.dao.TaskDao;
import com.tastmanagemt.task.management.dao.UserDao;
import com.tastmanagemt.task.management.model.Task;
import com.tastmanagemt.task.management.model.Task.TaskStatus;
import com.tastmanagemt.task.management.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskDao taskDao;
    private final UserDao userDao;
//SAVE TASK PROCESS - START
    public ResponseEntity<Task> createTask(Task task, Long userId) {
        // Validate deadline is in future
        if (task.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Deadline must be in the future");
        }

        if (userId != null) {
            User user = userDao.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedUser(user);
        }

        task.setStatus(TaskStatus.PENDING);
        task.setStartTime(LocalDateTime.now());
        task.setStatusUpdateTime(LocalDateTime.now());

        Task savedTask = taskDao.save(task);
        return ResponseEntity.ok(savedTask);
    }
    //SAVE TASK PROCESS - END
//    --------------------------------------------------------------------------------------------------
//UPDATE TASK STATUS PROCESS - START
    public ResponseEntity<Task> updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskDao.findById(taskId)
                .orElseThrow(() -> new RuntimeException("This Task is not found! "));

        if (status == TaskStatus.IN_PROGRESS) {
            task.setStartTime(LocalDateTime.now());
        } else if (status == TaskStatus.COMPLETED) {
            if (task.getStartTime() == null) {
                throw new IllegalStateException("Task must be in progress before completion");
            }
        }

        task.setStatus(status);
        // StatusUpdateTime will be auto-set by @PreUpdate
        return ResponseEntity.ok(taskDao.save(task));
    }
//    UPDATE TASK STATUS PROCESS - END
//    --------------------------------------------------------------------------------------------------
//GET TASKS PROCESS - START

    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskDao.findAll();
        return ResponseEntity.ok(tasks);
    }
//GET TASKS PROCESS - END
//    --------------------------------------------------------------------------------------------------
//FILTER TASKS PROCESS - START (BY Status)
    public ResponseEntity<List<Task>> getTasksByStatus(TaskStatus status) {
        List<Task> tasks = taskDao.findByStatus(status);
        return ResponseEntity.ok(tasks);
    }
//    FILTER TASKS PROCESS - END
//    --------------------------------------------------------------------------------------------------
//FILTER TASKS PROCESS - START (BY Assigned User)

    public ResponseEntity<List<Task>> getTasksByAssignedUser(Long userId) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        List<Task> tasks = taskDao.findByAssignedUser(userOptional.get());
        return ResponseEntity.ok(tasks);
    }
// FILTER TASKS PROCESS - END
//    --------------------------------------------------------------------------------------------------
//FILTER TASKS PROCESS - START (BY Assigned User and Status)
    public ResponseEntity<List<Task>> getTasksByUserAndStatus(Long userId, TaskStatus status) {
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        List<Task> tasks = taskDao.findByAssignedUserAndStatus(userOptional.get(), status);
        return ResponseEntity.ok(tasks);
    }
//PAGINATION
    public ResponseEntity<Page<Task>> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks = taskDao.findAll(pageable);
        return ResponseEntity.ok(tasks);
    }



// FILTER TASKS PROCESS - END
}