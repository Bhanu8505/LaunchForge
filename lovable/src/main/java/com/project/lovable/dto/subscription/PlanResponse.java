package com.project.lovable.dto.subscription;

public record PlanResponse(
        Long id,
        String name,
        Integer maxProject,
        Integer maxTokensPerDay,
        Boolean unlimitedAi,
        String price
) {
}
