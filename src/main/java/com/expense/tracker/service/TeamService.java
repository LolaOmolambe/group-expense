package com.expense.tracker.service;

import com.expense.tracker.dto.CreateTeamDTO;
import com.expense.tracker.entity.Team;
import com.expense.tracker.entity.TeamUser;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.DuplicateEntityException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.TeamRepository;
import com.expense.tracker.repository.TeamUserRepository;
import com.expense.tracker.repository.UserRepository;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TeamService implements ITeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private IUserService userService;

    private static final Logger logger = Logger.getLogger(TeamService.class.getName());

    @Override
    @Transactional
    public CreateTeamDTO createTeam(CreateTeamDTO teamDTO, Principal principal) throws DuplicateEntityException, AuthException {

        if(teamRepository.findByName(teamDTO.getTeamName()) != null) {
            throw new DuplicateEntityException("Team already exists.");
        }

        User user = userService.getUser(principal);

        if (user == null) {
            throw new AuthException("Unable to retrieve profile of logged in user.");
        }

        ModelMapper modelMapper = new ModelMapper();
        Team newTeam = modelMapper.map(teamDTO, Team.class);


        Team storedTeam = teamRepository.save(newTeam);

        TeamUser teamUser = new TeamUser(user, storedTeam, true);
        teamUserRepository.save(teamUser);

        CreateTeamDTO result = modelMapper.map(storedTeam, CreateTeamDTO.class);

        return result;

    }

    @Override
    public CreateTeamDTO getTeamById(Long id) throws ResourceNotFoundException{
        Optional<Team> team = teamRepository.findById(id);

        if(!team.isPresent()) {
            throw new ResourceNotFoundException("No team with ID: " + id);
        }

        ModelMapper modelMapper = new ModelMapper();
        CreateTeamDTO result = modelMapper.map(team.get(), CreateTeamDTO.class);

        return result;
    }

    @Override
    public Set<TeamUser> addUserToTeam(Long teamId, Long userId, Principal principal) throws ResourceNotFoundException, AuthException, DuplicateEntityException {

        Optional<Team> team = teamRepository.findById(teamId);

        if(!team.isPresent()) {
            throw new ResourceNotFoundException("Team does not exist.");
        }

        //Check if new memeber exists
        Optional<User> newMember = userRepository.findById(userId);

        if(!newMember.isPresent()) {
            throw new ResourceNotFoundException("User does not exist");
        }


        //Get the logged in user
        User user = userService.getUser(principal);

        if (user == null) {
            throw new AuthException("\"Unable to retrieve profile of logged in user.");
        }

        //Check if logged in user is in team
        TeamUser teamUser = teamUserRepository.findByTeamAndUser(teamId, user.getId());

        if(teamUser == null){
            throw new ResourceNotFoundException("You are not part of this team.");
        }

        //Check if user is an admin
        if(!teamUser.getIsAdmin()){
            throw new AuthException("Only An Admin of this team can add users");
        }


        Set<TeamUser> memberDetails = newMember.get().getTeamUsers();

        //Check if user is not already in team

        List<TeamUser> memberTeams = newMember.get().getTeamUsers()
                .stream()
                .filter(element -> {
                    return element.getTeam().getId() == team.get().getId();
                })
                .collect(Collectors.toList());

        if(memberTeams.size() > 0) {
            throw new DuplicateEntityException("User already in team");
        }

        TeamUser newTeamUser = new TeamUser(newMember.get(), team.get(), false);
        teamUserRepository.save(newTeamUser);



        return memberDetails;
    }



    @Override
    public void removeUserFromTeam(Long teamId, Long userId) {

    }


}
