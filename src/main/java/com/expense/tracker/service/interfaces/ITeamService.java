package com.expense.tracker.service.interfaces;

import com.expense.tracker.dto.CreateTeamDTO;

import java.security.Principal;

public interface ITeamService {
    CreateTeamDTO createTeam(CreateTeamDTO teamDTO, Principal principal);

    CreateTeamDTO getTeamById(Long id);

    void addUserToTeam(Long teamId, Long userId, Principal principal);

    void removeUserFromTeam(Long teamId, Long userId);
}
