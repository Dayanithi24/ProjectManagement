package com.trustrace.projectManagement.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustrace.projectManagement.department.Department;
import com.trustrace.projectManagement.project.Project;
import com.trustrace.projectManagement.project.ProjectService;
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

import java.util.ArrayList;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService us;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;

    @BeforeEach
    void setup(){
        user=new User("1",new Department("1","CSE",20),"Ram","USER","Male","123",new ArrayList<>());
    }

    @Test
    @WithMockUser
    void testGetAllUsers() throws Exception {
        List<User> ls= Arrays.asList(user);
        when(us.getAllUsers()).thenReturn(ls);

        mvc.perform(get("/user/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Ram"));

    }
    @Test
    @WithMockUser
    void testGetUser() throws Exception {
        when(us.getUserById("1")).thenReturn(user);
        this.mvc.perform(get("/user/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Ram"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"User saved successfully!","User Already exists!"})
    @WithMockUser
    void testAddUser_User_Exist_And_DoesNotExist(String string) throws Exception{
        when(us.insertUser(any(User.class))).thenReturn(string);
        mvc.perform(post("/user/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(string)));
    }
    @Test
    @WithMockUser
    void testUpdateUser() throws Exception{
        when(us.updateUser(any(String.class),any(User.class))).thenReturn(user);
        mvc.perform(put("/user/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Ram"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"User deleted successfully!","User not found!"})
    @WithMockUser
    void testDeleteUser_User_Exist_And_DoesNotExist(String string) throws Exception{
        when(us.deleteUser(any(String.class))).thenReturn(string);
        mvc.perform(delete("/user/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(string)));
    }
}
