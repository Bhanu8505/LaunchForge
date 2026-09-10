package com.project.lovable.service;

import com.project.lovable.dto.subscription.PlanLimitsResponse;
import com.project.lovable.dto.subscription.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsageOfUser(Long userId);

    PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
