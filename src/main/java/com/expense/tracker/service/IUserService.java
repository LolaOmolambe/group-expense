package com.expense.tracker.service;

import com.expense.tracker.dto.JwtResponse;
import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.SignupRequest;
import com.expense.tracker.entity.User;

import java.security.Principal;

public interface IUserService {
    User registerUser(SignupRequest signupRequest);
    JwtResponse loginUser(LoginRequest loginRequest);
    User getUser(Principal principal);
}
