package com.project.lovable.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
