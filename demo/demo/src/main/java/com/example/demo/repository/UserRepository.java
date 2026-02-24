package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);  // note: "Usename" nahi, "Username"

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    // save() method JpaRepository mein already hai, alag se declare mat kar
}