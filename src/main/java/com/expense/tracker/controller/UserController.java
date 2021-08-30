package com.expense.tracker.controller;

import com.expense.tracker.dto.*;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.DuplicateEntityException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping(path = "/team" )
    public Set<CreateTeamDTO> getTeamsBelongingToUser(Principal principal) throws ResourceNotFoundException {
        return userService.getTeamsForUser(principal);
    }

    //TODO: ONLY FOR ADMIN and logged in user
    @GetMapping(path = "/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping(path = "/update")
    public UserDTO  updateUser(Principal principal, @RequestBody UserDTO userDTO){
        return userService.updateUser(principal, userDTO);
    }

    @GetMapping()
    public List<UserDTO> getUsers(@RequestParam(value = "page", defaultValue = "0")int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size){
        return userService.getUsers(page,size);
    }



}
