package com.expense.tracker.service;

import com.expense.tracker.dto.StandupUpdateDTO;
import com.expense.tracker.dto.response.StandupUpdateResponseDTO;
import com.expense.tracker.entity.StandupUpdate;
import com.expense.tracker.entity.Team;
import com.expense.tracker.entity.TeamUser;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.StandupUpdateRepository;
import com.expense.tracker.repository.TeamRepository;
import com.expense.tracker.repository.TeamUserRepository;
import com.expense.tracker.service.interfaces.IStandupUpdateService;
import com.expense.tracker.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class StandupUpdateService implements IStandupUpdateService {

    private static final Logger logger = Logger.getLogger(StandupUpdateService.class.getName());
    @Autowired
    private StandupUpdateRepository standupUpdateRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamUserRepository teamUserRepository;
    @Autowired
    private IUserService userService;

    @Override
    public StandupUpdateDTO addUpdate(StandupUpdateDTO standupUpdateDTO, Principal principal) {

        Optional<Team> team = teamRepository.findById(standupUpdateDTO.getTeamId());

        if (!team.isPresent()) {
            throw new ResourceNotFoundException("Team does not exist.");
        }

        //Get the logged in user
        User user = new User();
        try {
            user = userService.getUser(principal);
        } catch (ResourceNotFoundException ex) {
            throw new AuthException("Unable to retrieve profile of logged in user.");
        }

        //Check if logged in user is in team
        TeamUser teamUser = teamUserRepository.findByTeamAndUser(team.get().getId(), user.getId());

        if (teamUser == null) {
            throw new ResourceNotFoundException("You are not part of this team.");
        }

        StandupUpdate standupUpdate = StandupUpdate.builder().description(standupUpdateDTO.getDescription()).teamUser(teamUser).build();

        ModelMapper modelMapper = new ModelMapper();

        StandupUpdate savedUpdate = standupUpdateRepository.save(standupUpdate);

        StandupUpdateDTO result = modelMapper.map(savedUpdate, StandupUpdateDTO.class);

        return result;
    }

    @Override
    public StandupUpdateDTO getSingleUpdate(Long id) {
        StandupUpdate standupUpdate = standupUpdateRepository.findById(id).get();

        if (standupUpdate == null) {
            throw new ResourceNotFoundException("No standup with ID: " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        StandupUpdateDTO result = modelMapper.map(standupUpdate, StandupUpdateDTO.class);
        return result;
    }

    @Override
    public Map<String, Object> getStandupForTeam(Long teamId, int page, int pageSize) {

        List<StandupUpdate> y = new ArrayList<>();

        if (page > 0) page -= 1;

        Page<StandupUpdate> standups = standupUpdateRepository.findByTeamID(teamId, PageRequest.of(page, pageSize));

        if (standups.isEmpty()) {
            throw new ResourceNotFoundException("Team does not exist.");
        }

        Optional<Team> team = teamRepository.findById(teamId);

        ModelMapper modelMapper = new ModelMapper();

        List<StandupUpdateResponseDTO> responses = standups.getContent().stream()
                .map(content -> modelMapper.map(content, StandupUpdateResponseDTO.class)).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("standups", responses);
        response.put("team", team.get().getName());
        response.put("currentPage", standups.getNumber());
        response.put("totalItems", standups.getTotalElements());
        response.put("totalPages", standups.getTotalPages());

        return response;

    }


}
