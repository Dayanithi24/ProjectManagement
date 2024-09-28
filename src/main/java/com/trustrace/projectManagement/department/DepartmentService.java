package com.trustrace.projectManagement.department;


import com.trustrace.projectManagement.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private MongoTemplate dr;
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public List<Department> getAllDepartments() {
        LOGGER.info("Get All Departments Entered...");
        return dr.findAll(Department.class);
    }

    public Department getDepartmentById(String id) {
        LOGGER.info("Get Departments By Id Entered...");
        return dr.findById(id,Department.class);
    }

    public String insertDepartment(Department department) {
        LOGGER.info("Insert Departments Entered...");
        if(!isDepartmentPresent(department.getId())){
            dr.save(department);
            return "Department added successfully!";
        }
        return "Department already exists!";
    }
    public boolean isDepartmentPresent(String id){
        LOGGER.info("Checking Departments Present or not Entered...");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return dr.exists(query, Department.class);
    }
    public Department updateDepartment(String id, Department department) {
        LOGGER.info("Update Departments Entered...");
        if(isDepartmentPresent(id)){
            Department d=getDepartmentById(id);
            d.setName(department.getName());
            d.setNoOfFaculties(department.getNoOfFaculties());
            return dr.save(d);
        }
        return null;
    }

    public String deleteDepartment(String id) {
        LOGGER.info("Delete Departments Entered...");
        if(isDepartmentPresent(id)){
            dr.remove(id);
            return "Department deleted successfully!";
        }
        return "Department not found!";
    }
}
