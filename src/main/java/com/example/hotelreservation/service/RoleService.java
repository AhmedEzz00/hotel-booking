package com.example.hotelreservation.service;

import com.example.hotelreservation.exception.RoleAlreadyExistException;
import com.example.hotelreservation.exception.UserAlreadyExistsException;
import com.example.hotelreservation.model.Role;
import com.example.hotelreservation.model.User;
import com.example.hotelreservation.repository.RoleRepository;
import com.example.hotelreservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role myRole) {
        String roleName= "ROLE_"+myRole.getName().toUpperCase();
        Role role= new Role(roleName);
        if (roleRepository.existsByName(role)){
            throw new RoleAlreadyExistException(role.getName()+ " role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user= userRepository.findById(userId);
        Optional<Role> role= roleRepository.findById(roleId);
        if(role.isPresent() &&user.isPresent()&& role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("user not found");
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user= userRepository.findById(userId);
        Optional<Role> role= roleRepository.findById(roleId);
        if(role.isPresent() &&user.isPresent()&& user.get().getRoles().contains(role.get())){
            throw new UserAlreadyExistsException(
                    user.get().getFirstName()+" is already assigned to the "+ role.get().getName()+" role");
        }
        if(role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());

        }
            return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role> optionalRole= roleRepository.findById(roleId);
        optionalRole.get().removeAllUsersFromRole();
        return roleRepository.save(optionalRole.get());
    }
}
