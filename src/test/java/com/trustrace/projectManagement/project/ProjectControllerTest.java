package com.trustrace.projectManagement.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustrace.projectManagement.department.Department;
import com.trustrace.projectManagement.department.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ProjectService ps;
    @Autowired
    private ObjectMapper objectMapper;
    private Project p;

    @BeforeEach
    void setup(){
        p=new Project("1","A",new Department("1","CSE",20));
    }

    @Test
    @WithMockUser
    void testGetAllProjects() throws Exception {
        List<Project> ls= Arrays.asList(p,
                new Project("2","B",new Department("1","CSE",20))
        );
        when(ps.getAllProjects()).thenReturn(ls);

        mvc.perform(get("/project/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("A"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("B"));
    }
    @Test
    @WithMockUser
    void testGetProject() throws Exception {
        when(ps.getProjectById("1")).thenReturn(p);
        this.mvc.perform(get("/project/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("A"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"Project added successfully!","Project id already exists!"})
    @WithMockUser
    void testAddProject_Project_Exist_And_DoesNotExist(String string) throws Exception{
        when(ps.insertProject(any(Project.class))).thenReturn(string);
        mvc.perform(post("/project/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(p)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(string)));
    }
    @Test
    @WithMockUser
    void testUpdateProject() throws Exception{
        when(ps.updateProject(any(String.class),any(Project.class))).thenReturn(p);
        mvc.perform(put("/project/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(p)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("A"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"Project deleted successfully!","Project doesn't exist!"})
    @WithMockUser
    void testDeleteProject_Project_Exist_And_DoesNotExist(String string) throws Exception{
        when(ps.deleteProject(any(String.class))).thenReturn(string);
        mvc.perform(delete("/project/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(string)));
    }
}
