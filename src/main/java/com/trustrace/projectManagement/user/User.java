package com.trustrace.projectManagement.user;

import com.trustrace.projectManagement.department.Department;
import com.trustrace.projectManagement.project.Project;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class User {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    private Department department;
    private String name;
    private String role;
    private String gender;
    private String password;
    @DocumentReference(lazy = true)
    private List<Project> projects;
}
