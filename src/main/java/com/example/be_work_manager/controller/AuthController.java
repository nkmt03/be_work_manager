package com.example.be_work_manager.controller;

import com.example.be_work_manager.dto.AuthRequest;
import com.example.be_work_manager.dto.AuthResponse;
import com.example.be_work_manager.model.User;
import com.example.be_work_manager.repository.UserRepository;
import com.example.be_work_manager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Void> deleteAccount(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        authService.deleteAccount(user.getId());
        return ResponseEntity.noContent().build();
    }
}