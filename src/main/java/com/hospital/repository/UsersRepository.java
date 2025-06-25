package com.hospital.repository;

import com.hospital.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    List<Users> findByRole(Users.Role role);  // Example: Find users by role
    Users findByUsername(String username);
      // Example: Find user by username
}
