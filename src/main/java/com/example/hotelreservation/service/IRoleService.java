package com.example.hotelreservation.service;

import com.example.hotelreservation.exception.RoleAlreadyExistException;
import com.example.hotelreservation.model.Role;
import com.example.hotelreservation.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IRoleService {
    List<Role> getRoles();
    Role createRole(Role role);
    void deleteRole(Long id);
    Role findByName(String name);
    User removeUserFromRole(Long userId,Long roleId);
    User assignRoleToUser(Long userId,Long roleId);
    Role removeAllUsersFromRole(Long roleId);

}
