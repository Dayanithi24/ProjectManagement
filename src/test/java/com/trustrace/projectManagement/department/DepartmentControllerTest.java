package com.trustrace.projectManagement.department;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private DepartmentService ds;
    @Autowired
    private ObjectMapper objectMapper;

    private Department d;

    @BeforeEach
    void setup(){
        d=new Department("1","CSE",20);
    }

    @Test
    @WithMockUser
    void testGetAllDepartments() throws Exception {
        List<Department> ls= Arrays.asList(
                d,
                new Department("2","IT",10)
        );
        when(ds.getAllDepartments()).thenReturn(ls);

        mvc.perform(get("/department/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("CSE"));
    }
    @Test
    @WithMockUser
    void testGetDepartment() throws Exception {
        when(ds.getDepartmentById("1")).thenReturn(d);
        this.mvc.perform(get("/department/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("CSE"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"Department added successfully!","Department already exists!"})
    @WithMockUser
    void testAddDepartment_Department_Exist_And_DoesNotExist(String string) throws Exception{
        when(ds.insertDepartment(any(Department.class))).thenReturn(string);
        mvc.perform(post("/department/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(d)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(string)));
    }
    @Test
    @WithMockUser
    void testUpdateDepartment() throws Exception{
        when(ds.updateDepartment(any(String.class),any(Department.class))).thenReturn(d);
        mvc.perform(put("/department/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(d)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("CSE"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"Department deleted successfully!","Department not found!"})
    @WithMockUser
    void testDeleteDepartment_Department_Exist_And_DoesNotExist(String string) throws Exception{
        when(ds.deleteDepartment(any(String.class))).thenReturn(string);
        mvc.perform(delete("/department/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(string)));
    }
}
