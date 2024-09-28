package com.trustrace.projectManagement.user;

import com.trustrace.projectManagement.department.Department;
import com.trustrace.projectManagement.department.DepartmentService;
import com.trustrace.projectManagement.project.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository ur;
    @Mock
    private DepartmentService ds;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ProjectService ps;
    @InjectMocks
    private UserService us;

    @Test
    void testGetAllUsers(){
        User u1=new User("1",null,"Ram","user","male","123",new ArrayList<>());
        User u2=new User("2",null,"Ravi","user","male","123",new ArrayList<>());
        List<User> ls= Arrays.asList(u1,u2);
        when(ur.findAll()).thenReturn(ls);
        List<User> result=us.getAllUsers();
        assertNotNull(result);
        assertEquals(2,result.size());
        verify(ur,times(1)).findAll();
    }
    @Test
    void testGetUserById(){
        User u1=new User("1",null,"Ram","user","male","123",null);
        when(ur.findById("1")).thenReturn(Optional.of(u1));
        User result=us.getUserById("1");
        assertNotNull(result);
        assertEquals("Ram",result.getName());
        verify(ur,times(1)).findById("1");
    }
    @Test
    void testInsertUser_UserExists(){
        User u1=new User("1",null,"Ram","user","male","123",null);
        when(ur.existsById("1")).thenReturn(true);
        String result = us.insertUser(u1);
        assertEquals("User Already exists!", result);
        verify(ur, never()).save(u1);
    }
    @Test
    void testInsertUser_UserDoesNotExists(){
        User u1=new User("1",null,"Ram","user","male","123",null);
        when(ur.existsById("1")).thenReturn(false);
        String result=us.insertUser(u1);
        assertEquals("User saved successfully!",result);
        verify(ur,times(1)).save(u1);
    }
    @Test
    void testUpdateUser_UserDoesNotExist(){
        User u1=new User("1",null,"Ram","user","male","123",null);
        when(ur.existsById("1")).thenReturn(false);
        User result=us.updateUser("1",u1);
        assertNull(result);
        verify(ur,never()).save(any());
    }
    @Test
    void testUpdateUser_UserExists(){
        User u1=new User("1",null,"Ram","user","male","123",new ArrayList<>());
        User u2=new User("1",new Department("1","CSE",20),"Ram","user","male","123",new ArrayList<>());
        when(ur.existsById("1")).thenReturn(true);
        when(ur.findById("1")).thenReturn(Optional.of(u1));
        when(ur.save(u1)).thenReturn(u1);
        User result=us.updateUser("1",u2);
        assertNotNull(result);
        verify(ur,times(1)).save(u1);
    }
    @Test
    void testDeleteUser_UserExists(){
        when(ur.existsById("1")).thenReturn(true);
        String result =us.deleteUser("1");
        assertEquals("User deleted successfully!", result);
        verify(ur, times(1)).deleteById("1");
    }
    @Test
    void testDeleteUser_UserDoesNotExist(){
        when(ur.existsById("1")).thenReturn(false);
        String result=us.deleteUser("1");
        assertEquals("User not found!", result);
        verify(ur, never()).deleteById("1");
    }
}
