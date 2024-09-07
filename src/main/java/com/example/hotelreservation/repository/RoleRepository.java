package com.example.hotelreservation.repository;

import com.example.hotelreservation.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String roleUser);

    boolean existsByName(Role role);
    //Optional<Role> findById(Long id);
}
