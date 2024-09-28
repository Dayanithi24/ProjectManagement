package com.trustrace.projectManagement.project;

import com.trustrace.projectManagement.department.Department;
import com.trustrace.projectManagement.department.DepartmentService;
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
public class ProjectService {
    @Autowired
    private MongoTemplate pr;
    @Autowired
    private DepartmentService ds;
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public List<Project> getAllProjects() {
        LOGGER.info("Get All Projects Entered...");
        return pr.findAll(Project.class);
    }

    public Project getProjectById(String id) {
        LOGGER.info("Get Projects By Id Entered...");
        return pr.findById(id,Project.class);
    }
    public boolean isProjectPresent(String id){
        LOGGER.info("Checking Project present Entered...");
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return pr.exists(query, Project.class);
    }
    public String insertProject(Project project) {
        LOGGER.info("Insert Projects Entered...");
        if(!isProjectPresent(project.getId())){
            ds.insertDepartment(project.getDepartment());
            pr.save(project);
            return "Project added successfully!";
        }
        return "Project id already exists!";
    }
    public String insertProjectFromUser(Project project, Department department) {
        LOGGER.info("Insert Projects From user Entered...");
        if(!isProjectPresent(project.getId())){
            project.setDepartment(department);
            pr.save(project);
            return "Project added successfully!";
        }
        return "Project id already exists!";
    }
    public Project updateProject(String id, Project project) {
        LOGGER.info("Update Projects Entered...");
        if(isProjectPresent(id)){
            Project p=getProjectById(id);
            p.setName(project.getName());
            if(project.getDepartment()!=null) {
                Department d = ds.updateDepartment(project.getDepartment().getId(), project.getDepartment());
                if (d == null) {
                    ds.insertDepartment(project.getDepartment());
                    d = ds.getDepartmentById(project.getDepartment().getId());
                }
                p.setDepartment(d);
            }
            return pr.save(p);
        }
        return null;
    }

    public String deleteProject(String id) {
        LOGGER.info("Delete Projects Entered...");
        if (isProjectPresent(id)) {
            pr.remove(id);
            return "Project deleted successfully!";
        }
        return "Project doesn't exist!";
    }
}
