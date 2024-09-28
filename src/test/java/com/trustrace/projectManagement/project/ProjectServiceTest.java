package com.trustrace.projectManagement.project;

import com.trustrace.projectManagement.department.Department;
import com.trustrace.projectManagement.department.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private MongoTemplate mt;
    @Mock
    private DepartmentService ds;
    @InjectMocks
    private ProjectService ps;

    @Test
    void testGetAllProjects(){
        Project p1=new Project("1","A",new Department("1","CSE",20));
        Project p2=new Project("2","B",null);
        List<Project> ls= Arrays.asList(p1,p2);
        when(mt.findAll(Project.class)).thenReturn(ls);
        List<Project> result=ps.getAllProjects();
        assertNotNull(result);
        assertEquals(2,result.size());
        verify(mt,times(1)).findAll(Project.class);
    }
    @Test
    void testGetProjectById(){
        Project p1=new Project("1","A",new Department("1","CSE",20));
        when(mt.findById("1",Project.class)).thenReturn(p1);
        Project result=ps.getProjectById("1");
        assertNotNull(result);
        assertEquals("A",result.getName());
        verify(mt,times(1)).findById("1",Project.class);
    }
    @Test
    void testInsertProject_ProjectDoesNotExist(){
        Project p=new Project("1","A",new Department("1","CSE",20));
        when(mt.exists(any(Query.class),eq(Project.class))).thenReturn(false);
        String result=ps.insertProject(p);
        assertEquals("Project added successfully!",result);
        verify(mt,times(1)).save(p);
    }
    @Test
    void testInsertProject_ProjectExists(){
        Project p=new Project("1","A",new Department("1","CSE",20));
        when(mt.exists(any(Query.class),eq(Project.class))).thenReturn(true);
        String result=ps.insertProject(p);
        assertEquals("Project id already exists!",result);
        verify(mt,never()).save(p);
    }
    @Test
    void testUpdateProject_ProjectDoesNotExist(){
        Project p=new Project("1","A",new Department("1","CSE",20));
        when(mt.exists(any(Query.class),eq(Project.class))).thenReturn(false);
        Project result=ps.updateProject("1",p);
        assertNull(result);
        verify(mt,never()).save(any());
    }
    @Test
    void testUpdateProject_ProjectExists(){
        Project p1=new Project("1","A",null);
        Project p2=new Project("1","A",new Department("1","CSE",20));
        when(mt.exists(any(Query.class),eq(Project.class))).thenReturn(true);
        when(mt.findById("1", Project.class)).thenReturn(p1);
        when(mt.save(p1)).thenReturn(p1);
        Project result=ps.updateProject("1",p2);
        assertNotNull(result);
        verify(mt,times(1)).save(p1);
    }
    @Test
    void testDeleteProject_ProjectExists(){
        when(mt.exists(any(Query.class), eq(Project.class))).thenReturn(true);
        String result = ps.deleteProject("1");
        assertEquals("Project deleted successfully!", result);
        verify(mt, times(1)).remove("1");
    }
    @Test
    void testDeleteProject_ProjectDoesNotExist(){
        when(mt.exists(any(Query.class),eq(Project.class))).thenReturn(false);
        String result=ps.deleteProject("1");
        assertEquals("Project doesn't exist!", result);
        verify(mt, never()).remove("1");
    }
}
