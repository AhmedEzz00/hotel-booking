package com.example.hotelreservation.service;

import com.example.hotelreservation.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IUserService {
    User registerUser(User user);

    List<User> getUsers();

    void deleteUser(String email);

    User getUser(String email);
}