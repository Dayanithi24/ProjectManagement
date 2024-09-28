package com.trustrace.projectManagement.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/department/")
public class DepartmentController {
    @Autowired
    private DepartmentService ds;
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments(){
        List<Department> ls=ds.getAllDepartments();
        return ResponseEntity.ok(ls);
    }
    @GetMapping("{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable("id") String id){
        Department Department=ds.getDepartmentById(id);
        return ResponseEntity.ok(Department);
    }
    @PostMapping
    public ResponseEntity<String> addDepartment(@RequestBody Department department){
        String res=ds.insertDepartment(department);
        return ResponseEntity.ok(res);
    }
    @PutMapping("{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable("id") String id,@RequestBody Department department){
        Department e=ds.updateDepartment(id,department);
        return ResponseEntity.ok(e);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("id") String id){
        String s=ds.deleteDepartment(id);
        return ResponseEntity.ok(s);
    }
}
