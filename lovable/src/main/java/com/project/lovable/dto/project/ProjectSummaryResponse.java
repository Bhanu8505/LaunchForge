package com.project.lovable.dto.project;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String projectName,
        Instant createdAt,
        Instant updatedAt
) {
}
