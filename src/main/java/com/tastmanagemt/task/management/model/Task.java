package com.tastmanagemt.task.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Long taskId;

    @NotBlank(message = "Task title is required")
    @Column(name = "title", length = 100)
    private String title;

    @NotBlank(message = "Task description is required")
    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status = TaskStatus.PENDING;


    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @NotNull(message = "Deadline is required")
    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "status_update_time")
    private LocalDateTime statusUpdateTime;

    public enum TaskStatus {
        PENDING, IN_PROGRESS, COMPLETED
    }

    @PreUpdate
    protected void onUpdate() {
        statusUpdateTime = LocalDateTime.now();
    }
}
