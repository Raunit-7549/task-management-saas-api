package com.shadow.taskmanagementsaasapi.service;

import com.shadow.taskmanagementsaasapi.model.Project;
import com.shadow.taskmanagementsaasapi.model.Task;
import com.shadow.taskmanagementsaasapi.repository.ProjectRepository;
import com.shadow.taskmanagementsaasapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task createTask(Task task){
        if (task.getProject() == null || task.getProject().getId() == null) {
            throw new IllegalArgumentException("Task must belong to a valid project ID.");
        }

        Long projectId = task.getProject().getId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        task.setProject(project);
        return taskRepository.save(task);
    }
}
