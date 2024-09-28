package com.trustrace.projectManagement.department;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @Mock
    private MongoTemplate mt;
    @InjectMocks
    private DepartmentService ds;

    @Test
    void testGetAllDepartments(){
        List<Department> ls= Arrays.asList(new Department("1","CSE",20),new Department("2","IT",10));
        when(mt.findAll(Department.class)).thenReturn(ls);
        List<Department> result=ds.getAllDepartments();
        assertNotNull(result);
        assertEquals(2,result.size());
        verify(mt,times(1)).findAll(Department.class);
    }
    @Test
    void testGetDepartmentById(){
        Department d=new Department("1","CSE",20);
        when(mt.findById("1", Department.class)).thenReturn(d);
        Department result=ds.getDepartmentById("1");
        assertNotNull(result);
        assertEquals(d,result);
        verify(mt,times(1)).findById("1", Department.class);
    }
    @Test
    void testInsertDepartment_DepartmentExists(){
        Department d=new Department("1","CSE",20);
        when(mt.exists(any(Query.class), eq(Department.class))).thenReturn(true);
        String result = ds.insertDepartment(d);
        assertEquals("Department already exists!", result);
        verify(mt, never()).save(d);
    }
    @Test
    void testInsertDepartment_DepartmentDoesNotExists(){
        Department d=new Department("1","CSE",20);
        when(mt.exists(any(Query.class),eq(Department.class))).thenReturn(false);
        String result=ds.insertDepartment(d);
        assertEquals("Department added successfully!",result);
        verify(mt,times(1)).save(d);
    }
    @Test
    void testUpdateDepartment_DepartmentExists(){
        Department d1=new Department("1","IT",10);
        Department d2=new Department("1","CSE",20);
        when(mt.exists(any(Query.class),eq(Department.class))).thenReturn(true);
        when(mt.findById("1", Department.class)).thenReturn(d1);
        when(mt.save(d1)).thenReturn(d1);
        Department result=ds.updateDepartment("1",d2);
        assertNotNull(result);
        assertEquals("CSE",result.getName());
        assertEquals(20,result.getNoOfFaculties());
        verify(mt,times(1)).save(d1);
    }
    @Test
    void testUpdateDepartment_DepartmentDoesNotExist(){
        Department d2=new Department("1","CSE",20);
        when(mt.exists(any(Query.class),eq(Department.class))).thenReturn(false);
        Department result=ds.updateDepartment("1",d2);
        assertNull(result);
        verify(mt,never()).save(any());
    }
    @Test
    void testDeleteDepartment_DepartmentExists(){
        when(mt.exists(any(Query.class), eq(Department.class))).thenReturn(true);
        String result = ds.deleteDepartment("1");
        assertEquals("Department deleted successfully!", result);
        verify(mt, times(1)).remove("1");
    }
    @Test
    void testDeleteDepartment_DepartmentDoesNotExist(){
        when(mt.exists(any(Query.class),eq(Department.class))).thenReturn(false);
        String result=ds.deleteDepartment("1");
        assertEquals("Department not found!", result);
        verify(mt, never()).remove("1");
    }
}
