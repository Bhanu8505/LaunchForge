package com.project.lovable.dto.member;

import com.project.lovable.enums.ProjectRole;

public record InviteMemberRequest(
        String email,
        ProjectRole role
) {
}
