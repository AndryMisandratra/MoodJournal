package com.moodjournal.backend.controller;

import com.moodjournal.backend.dto.request.LoginRequest;
import com.moodjournal.backend.dto.request.RegisterRequest;
import com.moodjournal.backend.dto.response.AuthResponse;
import com.moodjournal.backend.entity.User;
import com.moodjournal.backend.service.AuthService;
import com.moodjournal.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.registerUser(request);
        String token = authService.authenticateUser(new LoginRequest(request.getEmail(), request.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, user.getId(), user.getEmail(), user.getDisplayName()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.authenticateUser(request);
        User user = userService.findByEmail(request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getEmail(), user.getDisplayName()));
    }
}