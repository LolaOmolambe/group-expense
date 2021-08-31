package com.expense.tracker.service.interfaces;

import com.expense.tracker.dto.CreateTeamDTO;
import com.expense.tracker.dto.JwtResponse;
import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.UserDTO;
import com.expense.tracker.dto.request.SignupRequestModel;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.AuthException;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface IUserService {
    UserDTO registerUser(SignupRequestModel signupRequestModel);

    JwtResponse loginUser(LoginRequest loginRequest) throws AuthException;

    User getUser(Principal principal);

    UserDTO getUser(String email);

    Set<CreateTeamDTO> getTeamsForUser(Principal principal);

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Principal principal, UserDTO user);

    List<UserDTO> getUsers(int page, int pageSize);

    boolean verifyEmailToken(String token);
}
