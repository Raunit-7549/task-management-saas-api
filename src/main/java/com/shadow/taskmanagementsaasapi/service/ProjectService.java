package com.shadow.taskmanagementsaasapi.service;

import com.shadow.taskmanagementsaasapi.model.Project;
import com.shadow.taskmanagementsaasapi.model.User;
import com.shadow.taskmanagementsaasapi.repository.ProjectRepository;
import com.shadow.taskmanagementsaasapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<Project> getProjectsByUserId(Long userId) {
        return projectRepository.findByOwnerId(userId);
    }

    public Project createProject(Project project) {
        if (project.getOwner() == null || project.getOwner().getId() == null) {
            throw new IllegalArgumentException("Project must have a valid owner ID.");
        }

        Long ownerId = project.getOwner().getId();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + ownerId));

        project.setOwner(owner);
        return projectRepository.save(project);
    }

}
