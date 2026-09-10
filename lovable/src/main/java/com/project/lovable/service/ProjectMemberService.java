package com.project.lovable.service;

import com.project.lovable.dto.member.InviteMemberRequest;
import com.project.lovable.dto.member.MemberResponse;
import com.project.lovable.dto.member.UpdateMemberRoleRequest;
import com.project.lovable.dto.project.ProjectResponse;
import com.project.lovable.entity.ProjectMember;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId, Long userId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request, Long userId);

    MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId);
}
