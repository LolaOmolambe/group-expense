package com.expense.tracker.service;

import com.expense.tracker.dto.CreateTeamDTO;
import com.expense.tracker.entity.TeamUser;

import java.security.Principal;
import java.util.Set;

public interface ITeamService {
    CreateTeamDTO createTeam(CreateTeamDTO teamDTO, Principal principal);
    CreateTeamDTO getTeamById(Long id);
    Set<TeamUser> addUserToTeam(Long teamId, Long userId, Principal principal);
    void removeUserFromTeam(Long teamId, Long userId);
}
