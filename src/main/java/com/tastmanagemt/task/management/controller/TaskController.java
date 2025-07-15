package com.tastmanagemt.task.management.controller;

import com.tastmanagemt.task.management.model.Task;
import com.tastmanagemt.task.management.model.Task.TaskStatus;
import com.tastmanagemt.task.management.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
//Save New Task - Start
@PostMapping
public ResponseEntity<Task> createTask(
        @Valid @RequestBody Task task,
        @RequestParam(required = false) Long userId) {

    if (task.getDeadline() == null) {
        throw new IllegalArgumentException("Deadline is required");
    }

    return taskService.createTask(task, userId);
}

    //Save New Task - End ------------------------------------------------------------------------------------------

    //Update Task Status - Start
    @PutMapping("/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus status) {
        return taskService.updateTaskStatus(taskId, status);
    }

    //Update Task Status - ENd --------------------------------------------------------------------------------------

    //Get Task by ID - Start
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return taskService.getAllTasks(page, size);
    }

    //Get Task by ID - End ----------------------------------------------------------------------------------------

    //Filter Tasks - Start

    @GetMapping("/filter")
    public ResponseEntity<List<Task>> filterTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long userId) {
        if (status != null && userId != null) {
            return taskService.getTasksByUserAndStatus(userId, status);
        } else if (status != null) {
            return taskService.getTasksByStatus(status);
        } else if (userId != null) {
            return taskService.getTasksByAssignedUser(userId);
        }
        return taskService.getAllTasks();
    }
}