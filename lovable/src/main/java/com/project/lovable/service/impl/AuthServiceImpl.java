package com.project.lovable.service.impl;

import com.project.lovable.dto.auth.AuthResponse;
import com.project.lovable.dto.auth.LoginRequest;
import com.project.lovable.dto.auth.SignupRequest;
import com.project.lovable.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse signup(SignupRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
