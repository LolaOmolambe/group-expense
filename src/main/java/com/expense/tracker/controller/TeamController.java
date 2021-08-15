package com.expense.tracker.controller;

import com.expense.tracker.dto.AddUserToTeamDTO;
import com.expense.tracker.dto.CreateTeamDTO;
import com.expense.tracker.dto.response.Response;
import com.expense.tracker.entity.TeamUser;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.ITeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/team")
public class TeamController {

    @Autowired
    private ITeamService teamService;

    @PostMapping
    public CreateTeamDTO createTeam(@Validated @RequestBody CreateTeamDTO createTeamDTO, Principal principal) throws ResourceNotFoundException {
        CreateTeamDTO team = teamService.createTeam(createTeamDTO, principal);
        //return new Response<CreateTeamDTO>(HttpStatus.OK.toString(), "success", team);
        return  team;
    }

    @GetMapping(path = "/{id}" )
    public CreateTeamDTO getTeamById(@PathVariable Long id) throws ResourceNotFoundException {
        CreateTeamDTO result = teamService.getTeamById(id);
        //return new Response<CreateTeamDTO>(HttpStatus.OK.toString(), "success", result);
        return result;

    }

    @PostMapping(path = "/add_user_to_team")
    public Set<TeamUser> addUserToTeam(@Validated @RequestBody AddUserToTeamDTO addUserToTeamDTO, Principal principal) throws ResourceNotFoundException , AuthException {
        Set<TeamUser> team = teamService.addUserToTeam(addUserToTeamDTO.getTeamId(), addUserToTeamDTO.getUserId(), principal);
        //return new Response<Set<TeamUser>>(HttpStatus.OK.toString(), "success", team);
        return team;
    }
}
