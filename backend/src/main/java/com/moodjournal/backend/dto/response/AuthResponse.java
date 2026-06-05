package com.moodjournal.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String displayName;

    public AuthResponse(String accessToken, Long userId, String email, String displayName) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
    }
}