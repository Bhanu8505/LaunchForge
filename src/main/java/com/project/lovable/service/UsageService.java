package com.project.lovable.service;

import com.project.lovable.dto.subscription.PlanLimitsResponse;
import com.project.lovable.dto.subscription.UsageTodayResponse;

public interface UsageService {
    void recordTokenUsage(Long userId, int actualTokens);
    void checkDailyTokensUsage();
}
