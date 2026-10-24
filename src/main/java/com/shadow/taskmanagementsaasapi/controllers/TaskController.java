package com.shadow.taskmanagementsaasapi.controllers;

import com.shadow.taskmanagementsaasapi.model.Task;
import com.shadow.taskmanagementsaasapi.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(){
        return ResponseEntity.ok(taskService.getTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(Long id){
        return ResponseEntity.ok(taskService.getTaskById(id)); // service
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask){
        return ResponseEntity.ok(taskService.updateTask(id, updatedTask)); // service
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build(); // service
    }

}
