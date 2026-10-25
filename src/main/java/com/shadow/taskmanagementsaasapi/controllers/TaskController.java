package com.shadow.taskmanagementsaasapi.controllers;

import com.shadow.taskmanagementsaasapi.model.Task;
import com.shadow.taskmanagementsaasapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable Long projectId) {
        List<Task> tasks = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Task> createTask(@RequestBody Task task, Principal principal) {
        String managerEmail = principal.getName();
        return ResponseEntity.ok(taskService.createTask(task, managerEmail));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(){
        return ResponseEntity.ok(taskService.getTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask, Principal principal){
        String userEmail = principal.getName();
        return ResponseEntity.ok(taskService.updateTask(id, updatedTask, userEmail));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Principal principal){
        String userEmail = principal.getName();
        taskService.deleteTask(id, userEmail);
        return ResponseEntity.noContent().build();
    }

}
