package com.expense.tracker.controller;

import com.expense.tracker.dto.JwtResponse;
import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.Response;
import com.expense.tracker.dto.SignupRequest;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
    @Autowired
    private IUserService userService;


    @PostMapping("/signup")
    public Response<User> registerUser(@Validated @RequestBody SignupRequest signupRequest) throws ResourceNotFoundException {
        User user = userService.registerUser(signupRequest);
        return new Response<User>(HttpStatus.OK.toString(), "success", user);
    }

    @PostMapping("/login")
    public Response<JwtResponse> loginUser(@Validated @RequestBody LoginRequest loginRequest) {
        JwtResponse result = userService.loginUser(loginRequest);
        return new Response<JwtResponse>(HttpStatus.OK.toString(), "success", result);
    }
}
