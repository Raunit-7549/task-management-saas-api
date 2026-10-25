package com.shadow.taskmanagementsaasapi.service;

import com.shadow.taskmanagementsaasapi.model.Project;
import com.shadow.taskmanagementsaasapi.model.Task;
import com.shadow.taskmanagementsaasapi.model.User;
import com.shadow.taskmanagementsaasapi.repository.ProjectRepository;
import com.shadow.taskmanagementsaasapi.repository.TaskRepository;
import com.shadow.taskmanagementsaasapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task createTask(Task task, String managerEmail){
        if (task.getProject() == null || task.getProject().getId() == null) {
            throw new IllegalArgumentException("Task must belong to a valid project ID.");
        }

        Long projectId = task.getProject().getId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        if (project.getOwner() == null || !project.getOwner().getEmail().equals(managerEmail)) {
            throw new SecurityException("Unauthorized: You can only create tasks in your own projects.");
        }

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

    public Task updateTask(Long id, Task updatedTask, String userEmail) {
        Task existingTask = getTaskById(id);

        User currentUser = userRepository.findByEmail(userEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + userEmail);
        }

        boolean isProjectManager = existingTask.getProject() != null &&
                existingTask.getProject().getOwner() != null &&
                existingTask.getProject().getOwner().getEmail().equals(userEmail);

        if (isProjectManager) {
            if (updatedTask.getTitle() != null) {
                existingTask.setTitle(updatedTask.getTitle());
            }
            if (updatedTask.getDescription() != null) {
                existingTask.setDescription(updatedTask.getDescription());
            }
            if (updatedTask.getStatus() != null) {
                existingTask.setStatus(updatedTask.getStatus());
            }
            if (updatedTask.getPriority() != null) {
                existingTask.setPriority(updatedTask.getPriority());
            }
            if (updatedTask.getAssigned() != null) {
                if (updatedTask.getAssigned().getId() == null) {
                    existingTask.setAssigned(null);
                } else {
                    Long userId = updatedTask.getAssigned().getId();
                    User assignedUser = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
                    existingTask.setAssigned(assignedUser);
                }
            }
        } else {

            boolean isAssignedDeveloper = existingTask.getAssigned() != null &&
                    existingTask.getAssigned().getEmail().equals(userEmail);

            if (!isAssignedDeveloper) {
                throw new SecurityException("Unauthorized: You can only update status for tasks assigned to you.");
            }

            if (updatedTask.getStatus() != null) {
                existingTask.setStatus(updatedTask.getStatus());
            } else {
                throw new IllegalArgumentException("Developers can only update the task status.");
            }
        }

        return taskRepository.save(existingTask);
    }
    public void deleteTask(Long id, String managerEmail) {
        Task existingTask = getTaskById(id);

        if (existingTask.getProject() == null || existingTask.getProject().getOwner() == null ||
                !existingTask.getProject().getOwner().getEmail().equals(managerEmail)) {
            throw new SecurityException("Unauthorized: You can only delete tasks in your own projects.");
        }

        taskRepository.deleteById(id);
    }
}
