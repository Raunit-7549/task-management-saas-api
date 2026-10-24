package com.shadow.taskmanagementsaasapi.controllers;

import com.shadow.taskmanagementsaasapi.model.Project;
import com.shadow.taskmanagementsaasapi.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Project>> getProjectsByUserId(@PathVariable Long userId) {
        List<Project> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects(){
        return ResponseEntity.ok(projectService.getProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id){
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject){
        return ResponseEntity.ok(projectService.updateProject(id, updatedProject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
