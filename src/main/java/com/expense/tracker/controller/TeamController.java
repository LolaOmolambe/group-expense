package com.expense.tracker.controller;

import com.expense.tracker.dto.AddUserToTeamDTO;
import com.expense.tracker.dto.CreateTeamDTO;
import com.expense.tracker.entity.TeamUser;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.DuplicateEntityException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.interfaces.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path = "/api/v1/team")
public class TeamController {

    @Autowired
    private ITeamService teamService;

    @PostMapping
    public CreateTeamDTO createTeam(@Validated @RequestBody CreateTeamDTO createTeamDTO, Principal principal) throws DuplicateEntityException, AuthException {
        CreateTeamDTO team = teamService.createTeam(createTeamDTO, principal);
        return  team;
    }

    @GetMapping(path = "/{id}" )
    public CreateTeamDTO getTeamById(@PathVariable Long id) throws ResourceNotFoundException {
        CreateTeamDTO result = teamService.getTeamById(id);
        return result;

    }

    @PostMapping(path = "/add_user_to_team")
    public Set<TeamUser> addUserToTeam(@Validated @RequestBody AddUserToTeamDTO addUserToTeamDTO, Principal principal) throws ResourceNotFoundException , AuthException {
        Set<TeamUser> team = teamService.addUserToTeam(addUserToTeamDTO.getTeamId(), addUserToTeamDTO.getUserId(), principal);
        return team;
    }
}
