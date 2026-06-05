package com.moodjournal.backend.service;

import com.moodjournal.backend.dto.request.LoginRequest;
import com.moodjournal.backend.entity.User;
import com.moodjournal.backend.exception.InvalidCredentialsException;
import com.moodjournal.backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public String authenticateUser(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userService.findByEmail(request.getEmail());
            return tokenProvider.generateToken(user.getEmail(), user.getId());
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }
}