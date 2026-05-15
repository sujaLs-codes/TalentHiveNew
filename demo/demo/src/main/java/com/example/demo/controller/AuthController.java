package com.example.demo.controller;

import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequest request, HttpSession  session) {
        User user = userService.findByEmail(request.getUsernameOrEmail());
        if(user == null) {
            user = userService.findByUsername(request.getUsernameOrEmail());
        }

        if(user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid credentials"));
        }

        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole().name());

        return ResponseEntity.ok(new MessageResponse("Login successful! Welcome " + user.getUsername()));
    }

    // Current Logged-in User Details
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(UserResponse.from(user));
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpSession session) {
        session.invalidate();   // Session destroy kar deta hai
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }
}
