package com.example.hotelreservation.service;

import com.example.hotelreservation.exception.UserAlreadyExistsException;
import com.example.hotelreservation.model.Role;
import com.example.hotelreservation.model.User;
import com.example.hotelreservation.repository.RoleRepository;
import com.example.hotelreservation.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail()+ " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole= roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {

        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User user= getUser(email);
        if (user!= null){
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public User getUser(String email) {

        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found"));
    }
}
