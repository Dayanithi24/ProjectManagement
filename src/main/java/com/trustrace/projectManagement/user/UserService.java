package com.trustrace.projectManagement.user;

import com.trustrace.projectManagement.department.Department;
import com.trustrace.projectManagement.department.DepartmentService;
import com.trustrace.projectManagement.project.Project;
import com.trustrace.projectManagement.project.ProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    private final UserRepository ur;
    private final DepartmentService ds;
    private final ProjectService ps;
    private final PasswordEncoder passwordEncoder;
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository ur, DepartmentService ds, ProjectService ps, PasswordEncoder passwordEncoder) {
        this.ur = ur;
        this.ds = ds;
        this.ps = ps;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        LOGGER.info("Getting all the users");
        return ur.findAll();
    }

    public User getUserById(String id) {
        LOGGER.info("Getting user of the id : {}",id);
        return ur.findById(id).orElse(null);
    }

    public String insertUser(User user) {
        LOGGER.info("Inserting User...");
        if(!isUserPresent(user.getId())){
            LOGGER.info("User is present...");
            if(user.getDepartment()!=null) {
                LOGGER.info("Department is not null..");
                ds.insertDepartment(user.getDepartment());
            }
            if(user.getProjects()!=null) {
                LOGGER.info("Found some projects...");
                for (Project p : user.getProjects())
                    ps.insertProjectFromUser(p, user.getDepartment());
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println("Encoded Password: " + user.getPassword());
            ur.save(user);
            LOGGER.info("User saved..");
            return "User saved successfully!";
        }
        LOGGER.info("User Already exists!");
        return "User Already exists!";
    }

    public boolean isUserPresent(String id){
        LOGGER.info("Is User Present Entered...");
        return ur.existsById(id);
    }
    public User updateUser(String id, User user) {
        LOGGER.info("Updating User Entered...");
        if(isUserPresent(id)){
            User u=getUserById(id);
            u.setName(user.getName());
            u.setGender(user.getGender());
            u.setRole(user.getRole());
            u.setPassword(passwordEncoder.encode(user.getPassword()));
            List<Project> ls=new ArrayList<>();
            for(Project p: user.getProjects()){
                Project pp=ps.updateProject(p.getId(),p);
                if(pp==null){
                    ps.insertProject(p);
                    pp=ps.getProjectById(p.getId());
                }
                ls.add(pp);
            }
            u.setProjects(ls);
            Department d=ds.updateDepartment(user.getDepartment().getId(),user.getDepartment());
            if(d==null){
                ds.insertDepartment(user.getDepartment());
                d=ds.getDepartmentById(user.getDepartment().getId());
            }
            u.setDepartment(d);
            return ur.save(u);
        }
        return null;
    }
    public User findByName(String name) {
        LOGGER.info("Finding By name Entered...");
        User user = ur.findByName(name);
        if (user != null) {
            System.out.println("User found: " + user.getName());
        } else {
            System.out.println("User not found: " + name);
        }
        return user;
    }

    public String deleteUser(String id) {
        LOGGER.info("Deleting user Entered..");
        if(isUserPresent(id)){
            ur.deleteById(id);
            return "User deleted successfully!";
        }
        return "User not found!";
    }
}
