package com.example.be_work_manager.controller;

import com.example.be_work_manager.dto.AuthRequest;
import com.example.be_work_manager.dto.AuthResponse;
import com.example.be_work_manager.model.User;
import com.example.be_work_manager.repository.UserRepository;
import com.example.be_work_manager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.register(authRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<Void> deleteAccount(@RequestHeader("Session-Id") String sessionId) {
        Long userId = authService.getUserIdFromSession(sessionId);
        authService.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }
}