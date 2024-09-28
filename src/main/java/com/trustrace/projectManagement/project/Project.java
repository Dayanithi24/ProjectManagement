package com.trustrace.projectManagement.project;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trustrace.projectManagement.department.Department;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@AllArgsConstructor
@Data
public class Project {
    @Id
    private String id;
    private String name;
//    @JsonIgnore
    @JsonBackReference
    @DocumentReference(lazy = true)
    private Department department;

}
