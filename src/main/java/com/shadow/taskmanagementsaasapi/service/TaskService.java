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

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);

        if (updatedTask.getTitle() != null) {
            existingTask.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }
        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}
