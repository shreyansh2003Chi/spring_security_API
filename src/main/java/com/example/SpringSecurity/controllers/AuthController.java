package com.example.SpringSecurity.controllers;

import com.example.SpringSecurity.config.JwtService;
import com.example.SpringSecurity.dtos.AuthRequest;
import com.example.SpringSecurity.dtos.AuthResponse;
import com.example.SpringSecurity.dtos.SignupRequest;
import com.example.SpringSecurity.models.Role;
import com.example.SpringSecurity.models.User;
import com.example.SpringSecurity.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public String register(@RequestBody SignupRequest signupRequest) {

        User user = new User();
        user.setUsername(signupRequest.getUsername());

        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        user.setRole(Role.USER);

        userRepository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        var user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow();
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credential!");
        }
        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }


}
