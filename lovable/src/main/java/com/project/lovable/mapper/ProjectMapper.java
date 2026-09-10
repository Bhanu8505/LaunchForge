package com.project.lovable.mapper;

import com.project.lovable.dto.project.ProjectResponse;
import com.project.lovable.dto.project.ProjectSummaryResponse;
import com.project.lovable.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    @Mapping(target="projectName",source = "name")
    ProjectSummaryResponse toProjectSummaryResponse(Project project);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> REDACTED);

}
