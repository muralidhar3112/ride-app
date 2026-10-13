package com.rider.ride_app.controller;

import com.rider.ride_app.dto.LoginRequest;
import com.rider.ride_app.dto.LoginResponse;
import com.rider.ride_app.dto.UserRegistrationRequest;
import com.rider.ride_app.dto.UserResponse;
import jakarta.validation.Valid;
import com.rider.ride_app.entity.User;
import com.rider.ride_app.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse createUser(
            @Valid @RequestBody UserRegistrationRequest request) {

        return userService.createUser(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return userService.login(request);
    }

    @GetMapping("/rider-test")
    @PreAuthorize("hasRole('RIDER')")
    public String riderTest() {
        return "You are authorized as a RIDER";
    }
}