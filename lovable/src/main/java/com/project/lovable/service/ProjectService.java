package com.project.lovable.service;

import com.project.lovable.dto.project.ProjectRequest;
import com.project.lovable.dto.project.ProjectResponse;
import com.project.lovable.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse getUserProjectById(Long id, Long userId);

    List<ProjectSummaryResponse> getUserProjects(Long userId);

    ProjectResponse createProject(ProjectRequest request, Long userId);

    ProjectResponse updateProject(Long id, ProjectRequest request, Long userId);

    void softProject(Long id, Long userId);
}
