package com.example.be_work_manager.controller;

import com.example.be_work_manager.dto.TaskDTO;
import com.example.be_work_manager.model.User;
import com.example.be_work_manager.repository.UserRepository;
import com.example.be_work_manager.service.AuthService;
import com.example.be_work_manager.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/upcoming-tasks")
    public ResponseEntity<List<TaskDTO>> getUpcomingTasks(
            @RequestHeader("Session-Id") String sessionId, @RequestParam String dueDate) {
        Long userId = authService.getUserIdFromSession(sessionId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(notificationService.getUpcomingTasks(user.getId(), LocalDateTime.parse(dueDate)));
    }
}