package com.expense.tracker.service;

import com.expense.tracker.dto.CreateTeamDTO;
import com.expense.tracker.dto.UserDTO;
import com.expense.tracker.entity.Team;
import com.expense.tracker.entity.TeamUser;
import com.expense.tracker.entity.User;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.DuplicateEntityException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.TeamRepository;
import com.expense.tracker.repository.TeamUserRepository;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.service.interfaces.ITeamService;
import com.expense.tracker.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        try{
            logger.log(Level.INFO, "Creating Team Service");

            if (teamRepository.findByName(teamDTO.getTeamName()) != null) {
                logger.log(Level.SEVERE, "Team already exists.");
                throw new DuplicateEntityException("Team already exists.");
            }

            User user = userService.getUser(principal);
            if (user == null) {
                logger.log(Level.SEVERE, "Unable to retrieve profile of logged in user.");
                throw new AuthException("Unable to retrieve profile of logged in user.");
            }
            ModelMapper modelMapper = new ModelMapper();
            Team newTeam = modelMapper.map(teamDTO, Team.class);

            Team storedTeam = teamRepository.save(newTeam);

            TeamUser teamUser = TeamUser.builder().user(user).team(storedTeam).isAdmin(true).build();
            teamUserRepository.save(teamUser);

            CreateTeamDTO result = modelMapper.map(storedTeam, CreateTeamDTO.class);

            return result;
        } catch(AuthException ex){
            throw new AuthException(ex.getMessage());
        }
        catch(DuplicateEntityException ex){
            throw new DuplicateEntityException(ex.getMessage());
        }
    }

    @Override
    public CreateTeamDTO getTeamById(Long id) throws ResourceNotFoundException {
        Team team = teamRepository.findById(id).get();

        if (team == null) {
            throw new ResourceNotFoundException("No team with ID: " + id);
        }

        ModelMapper modelMapper = new ModelMapper();

        Set<UserDTO> users = team.getTeamUsers()
                .stream()
                .map(teamUser -> {
                    return modelMapper.map(teamUser.getUser(), UserDTO.class);
                })
                .collect(Collectors.toSet());

        CreateTeamDTO result = modelMapper.map(team, CreateTeamDTO.class);

        result.setUsers(users);

        return result;
    }

    @Override
    public Set<TeamUser> addUserToTeam(Long teamId, Long userId, Principal principal) throws ResourceNotFoundException, AuthException, DuplicateEntityException {
        try{
            Optional<Team> team = teamRepository.findById(teamId);

            if (!team.isPresent()) {
                throw new ResourceNotFoundException("Team does not exist.");
            }

            Optional<User> newMember = userRepository.findById(userId);

            if (!newMember.isPresent()) {
                throw new ResourceNotFoundException("User does not exist");
            }

            //Get the logged in user
            User loggedInUser = userService.getUser(principal);

            if (loggedInUser == null) {
                throw new AuthException("Unable to retrieve profile of logged in user.");
            }

            //Check if logged in user is in team
            TeamUser teamUser = teamUserRepository.findByTeamAndUser(teamId, loggedInUser.getId());

            if (teamUser == null) {
                throw new ResourceNotFoundException("You are not part of this team. You can not add another member");
            }

            //Check if user is an admin
            if (!teamUser.getIsAdmin()) {
                throw new AuthException("Only An Admin of this team can add users");
            }

            Set<TeamUser> memberDetails = newMember.get().getTeamUsers();

            List<TeamUser> memberTeams = newMember.get().getTeamUsers()
                    .stream()
                    .filter(element -> {
                        return element.getTeam().getId() == team.get().getId();
                    })
                    .collect(Collectors.toList());

            if (memberTeams.size() > 0) {
                throw new DuplicateEntityException("User already in team");
            }

            TeamUser newTeamUser = TeamUser.builder().user(newMember.get()).team(team.get()).isAdmin(false).build();
            teamUserRepository.save(newTeamUser);

            return memberDetails;

        } catch(AuthException ex){
            throw new AuthException(ex.getMessage());
        }

    }

    @Override
    public void removeUserFromTeam(Long teamId, Long userId) {

    }


}
