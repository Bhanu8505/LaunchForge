package com.project.lovable.service.impl;

import com.project.lovable.dto.project.ProjectRequest;
import com.project.lovable.dto.project.ProjectResponse;
import com.project.lovable.dto.project.ProjectSummaryResponse;
import com.project.lovable.entity.Project;
import com.project.lovable.entity.User;
import com.project.lovable.mapper.ProjectMapper;
import com.project.lovable.repository.ProjectRepository;
import com.project.lovable.repository.UserRepository;
import com.project.lovable.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {

        User owner = userRepository.findById(userId).orElseThrow();

        Project project = Project.builder()
                .name(request.name())
                .owner(owner)
                .isPublic(false)
                .build();

        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);

    }

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {

        var REDACTED = projectRepository.findAllAccessibleByUser(userId);

        return projectMapper.toListOfProjectSummaryResponse(REDACTED);
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {
        return null;
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        return null;
    }

    @Override
    public void softProject(Long id, Long userId) {

    }
}
