package com.example.be_work_manager.repository;

import com.example.be_work_manager.model.Task;
import com.example.be_work_manager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskNotificationRepository extends JpaRepository<Task, Long> {
    List<Task> findByDueDateBeforeAndStatusAndUserId(LocalDateTime dueDate, TaskStatus status, Long userId);
}