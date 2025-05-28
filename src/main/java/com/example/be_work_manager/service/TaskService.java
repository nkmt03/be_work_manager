package com.example.be_work_manager.service;

import com.example.be_work_manager.dto.TaskDTO;
import com.example.be_work_manager.model.Task;
import com.example.be_work_manager.model.TaskStatus;
import com.example.be_work_manager.model.User;
import com.example.be_work_manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setCategory(taskDTO.getCategory());
        task.setStatus(TaskStatus.NOT_COMPLETED);
        task.setReminderTime(taskDTO.getReminderTime());
        task.setIcon(taskDTO.getIcon());
        task.setUser(new User() {{ setId(userId); }});

        task = taskRepository.save(task);
        taskDTO.setId(task.getId());
        taskDTO.setStatus(task.getStatus().name());
        taskDTO.setUserId(userId);
        return taskDTO;
    }

    public List<TaskDTO> getTasks(Long userId) {
        return taskRepository.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        return toDTO(task);
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDTO, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setCategory(taskDTO.getCategory());
        task.setReminderTime(taskDTO.getReminderTime());
        task.setIcon(taskDTO.getIcon());
        task.setUpdatedAt(java.time.LocalDateTime.now());

        task = taskRepository.save(task);
        return toDTO(task);
    }

    public void deleteTask(Long id, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        taskRepository.deleteById(id);
    }

    public TaskDTO completeTask(Long id, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }
        task.setStatus(TaskStatus.COMPLETED);
        task.setUpdatedAt(java.time.LocalDateTime.now());
        task = taskRepository.save(task);
        return toDTO(task);
    }

    public TaskDTO toDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setCategory(task.getCategory());
        taskDTO.setStatus(task.getStatus().name());
        taskDTO.setReminderTime(task.getReminderTime());
        taskDTO.setIcon(task.getIcon());
        taskDTO.setUserId(task.getUser().getId());
        return taskDTO;
    }
}