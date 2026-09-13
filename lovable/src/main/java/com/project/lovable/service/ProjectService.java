package com.project.lovable.service;

import com.project.lovable.dto.project.ProjectRequest;
import com.project.lovable.dto.project.ProjectResponse;
import com.project.lovable.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse getUserProjectById(Long id);

    List<ProjectSummaryResponse> getUserProjects();

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void softDeleteProject(Long id);
}
