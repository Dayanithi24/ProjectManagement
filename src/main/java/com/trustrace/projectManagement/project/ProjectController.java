package com.trustrace.projectManagement.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project/")
public class ProjectController {
    @Autowired
    private ProjectService ps;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(){
        List<Project> ls=ps.getAllProjects();
        return ResponseEntity.ok(ls);
    }
    @GetMapping("{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") String id){
        Project employee=ps.getProjectById(id);
        return ResponseEntity.ok(employee);
    }
    @PostMapping
    public ResponseEntity<String> addProject(@RequestBody Project project){
        String res=ps.insertProject(project);
        return ResponseEntity.ok(res);
    }
    @PutMapping("{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") String id, @RequestBody Project project){
        Project e=ps.updateProject(id,project);
        return ResponseEntity.ok(e);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProject(@PathVariable("id") String id){
        String s=ps.deleteProject(id);
        return ResponseEntity.ok(s);
    }
}
