package com.project.lovable.service.impl;

import com.project.lovable.dto.auth.UserProfileResponse;
import com.project.lovable.dto.subscription.PlanLimitsResponse;
import com.project.lovable.dto.subscription.UsageTodayResponse;
import com.project.lovable.error.ResourceNotFoundException;
import com.project.lovable.repository.UserRepository;
import com.project.lovable.service.UsageService;
import com.project.lovable.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User",username));
    }
}
