package com.example.be_work_manager.repository;

import com.example.be_work_manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Task, Long> {
    List<Task> findByDueDateBeforeAndStatusAndUserId(LocalDateTime dueDate, String status, Long userId);
}