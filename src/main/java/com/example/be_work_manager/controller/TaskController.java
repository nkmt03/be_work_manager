package com.example.be_work_manager.controller;

import com.example.be_work_manager.dto.TaskDTO;
import com.example.be_work_manager.model.User;
import com.example.be_work_manager.repository.UserRepository;
import com.example.be_work_manager.service.AuthService;
import com.example.be_work_manager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/create-task")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, @RequestHeader("Session-Id") String sessionId) {
        Long userId = authService.getUserIdFromSession(sessionId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        TaskDTO createdTask = taskService.createTask(taskDTO, user.getId());
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<List<TaskDTO>> getTasks(@RequestHeader("Session-Id") String sessionId) {
        Long userId = authService.getUserIdFromSession(sessionId);
        return ResponseEntity.ok(taskService.getTasks(userId));
    }

    @GetMapping("/get-task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id, @RequestHeader("Session-Id") String sessionId) {
        Long userId = authService.getUserIdFromSession(sessionId);
        return ResponseEntity.ok(taskService.getTaskById(id, userId));
    }

    @PutMapping("/update-task/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO, @RequestHeader("Session-Id") String sessionId) {
        Long userId = authService.getUserIdFromSession(sessionId);
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO, userId));
    }

    @DeleteMapping("/delete-task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @RequestHeader("Session-Id") String sessionId) {
        Long userId = authService.getUserIdFromSession(sessionId);
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/complete-task/{id}")
    public ResponseEntity<TaskDTO> completeTask(@PathVariable Long id, @RequestHeader("Session-Id") String sessionId) {
        Long userId = authService.getUserIdFromSession(sessionId);
        return ResponseEntity.ok(taskService.completeTask(id, userId));
    }
}