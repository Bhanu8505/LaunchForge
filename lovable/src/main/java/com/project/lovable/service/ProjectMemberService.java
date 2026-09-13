package com.project.lovable.service;

import com.project.lovable.dto.member.InviteMemberRequest;
import com.project.lovable.dto.member.MemberResponse;
import com.project.lovable.dto.member.UpdateMemberRoleRequest;
import com.project.lovable.dto.project.ProjectResponse;
import com.project.lovable.entity.ProjectMember;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request);

    void removeProjectMember(Long projectId, Long memberId);
}
