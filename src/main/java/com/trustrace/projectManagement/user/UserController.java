package com.trustrace.projectManagement.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Tag(name = "User Controller", description = "Actions on user")
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService us;
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> ls=us.getAllUsers();
        return ResponseEntity.ok(ls);
    }
    @GetMapping("{id}")
    @Operation(summary = "Get user", description = "Enter ID to get the specific user")
    public ResponseEntity<User> getUser(@PathVariable("id") String id){
        User user=us.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user){
        String res=us.insertUser(user);
        return ResponseEntity.ok(res);
    }
    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id,@RequestBody User user){
        User e=us.updateUser(id,user);
        return ResponseEntity.ok(e);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id){
        String s=us.deleteUser(id);
        return ResponseEntity.ok(s);
    }
}
