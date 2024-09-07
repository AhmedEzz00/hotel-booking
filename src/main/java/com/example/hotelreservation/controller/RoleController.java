package com.example.hotelreservation.controller;

import com.example.hotelreservation.exception.RoleAlreadyExistException;
import com.example.hotelreservation.model.Role;
import com.example.hotelreservation.model.User;
import com.example.hotelreservation.service.IRoleService;
import com.example.hotelreservation.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
    private final IRoleService roleService;
    private final IUserService userService;

    @GetMapping("/all-roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), FOUND);
    }

    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Role role){
        try {
            roleService.createRole(role);
            return  ResponseEntity.ok("New role created successfully!");
        }catch (RoleAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }

    @PostMapping("/remove-all-users-from-role/{roleId}")
    public Role deleteAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return  roleService.removeAllUsersFromRole(roleId);
    }


    @PostMapping("/remove-user-from-role")
    public User removeUserFromRole(@RequestParam("userId") Long userId,@RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId,roleId);
    }

    @PostMapping("/assign-user-to-role")
    public  User assignRoleToUser(@RequestParam("userId") Long userId,@RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId,roleId);
    }
}














