package com.project.lovable.mapper;

import com.project.lovable.dto.member.MemberResponse;
import com.project.lovable.entity.ProjectMember;
import com.project.lovable.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "projectRole", constant = "OWNER")
    MemberResponse toProjectMemberResponseFromOwner(User owner);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "projectRole", source = "projectRole")
    MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);

}
