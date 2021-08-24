package com.expense.tracker.controller;

import com.expense.tracker.dto.JwtResponse;
import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.SignupRequest;
import com.expense.tracker.dto.UserDTO;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.DuplicateEntityException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/v1/auth")
public class UserController {
    @Autowired
    private IUserService userService;


    @PostMapping(path = "/signup")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDTO registerUser(@Validated @RequestBody SignupRequest signupRequest) throws DuplicateEntityException, Exception {
        return userService.registerUser(signupRequest);
    }

    @PostMapping(path = "/login")
    @ResponseStatus(code = HttpStatus.OK)
    public JwtResponse loginUser(@Validated @RequestBody LoginRequest loginRequest) throws AuthException {
        return userService.loginUser(loginRequest);
    }


}
