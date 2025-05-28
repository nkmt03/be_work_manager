package com.example.be_work_manager.service;

import com.example.be_work_manager.dto.AuthRequest;
import com.example.be_work_manager.dto.AuthResponse;
import com.example.be_work_manager.model.User;
import com.example.be_work_manager.repository.TaskRepository;
import com.example.be_work_manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lưu sessionId và userId (thay cho JWT)
    private final Map<String, Long> sessions = new HashMap<>();

    public AuthResponse register(AuthRequest authRequest) {
        if (userRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email exists");
        }
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setName(authRequest.getName());
        userRepository.save(user);

        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user.getId());

        AuthResponse response = new AuthResponse();
        response.setToken(sessionId);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        return response;
    }

    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user.getId());

        AuthResponse response = new AuthResponse();
        response.setToken(sessionId);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        return response;
    }

    public void deleteAccount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        taskRepository.deleteAll(taskRepository.findByUserId(userId));
        userRepository.deleteById(userId);
    }

    public Long getUserIdFromSession(String sessionId) {
        Long userId = sessions.get(sessionId);
        if (userId == null) {
            throw new RuntimeException("Invalid session");
        }
        return userId;
    }
}