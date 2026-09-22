package com.project.lovable.dto.subscription;

public record PlanLimitsResponse(
        String planName,
        Integer maxTokensPerDay,
        Integer maxProject,
        Boolean unlimitedAi
) {
}
