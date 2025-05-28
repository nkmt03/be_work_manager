package com.example.be_work_manager.service;

import com.example.be_work_manager.dto.TaskDTO;
import com.example.be_work_manager.model.Task;
import com.example.be_work_manager.model.TaskStatus;
import com.example.be_work_manager.repository.TaskNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private TaskNotificationRepository notificationRepository;

    @Autowired
    private TaskService taskService;

    public List<TaskDTO> getUpcomingTasks(Long userId, LocalDateTime dueDate) {
        List<Task> tasks = notificationRepository.findByDueDateBeforeAndStatusAndUserId(
                dueDate, TaskStatus.NOT_COMPLETED, userId);
        return tasks.stream().map(task -> taskService.toDTO(task)).collect(Collectors.toList());
    }
}