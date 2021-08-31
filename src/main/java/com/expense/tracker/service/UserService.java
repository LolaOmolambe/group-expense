package com.expense.tracker.service;

import com.expense.tracker.dto.*;
import com.expense.tracker.dto.request.SignupRequestModel;
import com.expense.tracker.entity.Role;
import com.expense.tracker.entity.User;
import com.expense.tracker.enums.RoleType;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.DuplicateEntityException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.RoleRepository;
import com.expense.tracker.repository.TeamUserRepository;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.security.jwt.JwtUtils;
import com.expense.tracker.security.services.UserDetailsImpl;
import com.expense.tracker.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public UserDTO registerUser(SignupRequestModel signupRequestModel) {

        logger.log(Level.INFO, "Registering User");

        if (userRepository.existsByEmail(signupRequestModel.getEmail())) {
            logger.log(Level.SEVERE, "Email already exists");
            throw new DuplicateEntityException("Email already exists");
        }

        //Create Account
        User user = User.builder().firstName(signupRequestModel.getFirstName())
                .lastName(signupRequestModel.getLastName())
                .email(signupRequestModel.getEmail())
                .password(encoder.encode(signupRequestModel.getPassword()))
                .build();

        Set<String> strRoles = signupRequestModel.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.User)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
            roles.add(userRole);

        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleType.Admin)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "super_admin":
                        Role superRole = roleRepository.findByName(RoleType.SuperAdmin)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        roles.add(superRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleType.User)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        roles.add(userRole);
                        break;
                }
            });
        }

        user.setRoles(roles);
        user.setEmailVerificationStatus(Boolean.FALSE);
        user.setEmailVerificationToken(jwtUtils.generateEmailVerificationToken(user.getEmail()));
        userRepository.save(user);

        ModelMapper modelMapper = new ModelMapper();

        UserDTO result = modelMapper.map(user, UserDTO.class);

        //TODO Verify Email

        logger.log(Level.INFO, "User registered successfully");
        return result;
    }

    @Override
    public JwtResponse loginUser(LoginRequest loginRequest) throws AuthException {

        logger.log(Level.INFO, "Logging User in");
        Authentication authentication = null;

        try{
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch(AuthenticationException ex){
            logger.log(Level.SEVERE, "Authentication failed: no credentials provided");
            throw new AuthException("Invalid Credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        Date expiresIn = jwtUtils.getExpirationFromJwtToken(jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.getEmailVerificationStatus()) {
            //not verified
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        logger.log(Level.INFO, "Login Successful");

        return JwtResponse.builder().token(jwt)
                .id(userDetails.getId())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .roles(roles).expiresIn(expiresIn)
                .type("Bearer")
                .build();

    }

    @Override
    public User getUser(Principal principal) {
        String loggedInUser = jwtUtils.getAuthDetails();
        User user = this.userRepository.findByEmail(loggedInUser).orElse(null);

        if (user == null) {
            throw new ResourceNotFoundException("Invalid user.");
        } else {
            return user;
        }
    }

    @Override
    public UserDTO getUser(String email) {
        User user = userRepository.findByEmail(email).get();

        if (user == null) {
            throw new ResourceNotFoundException("Email Record does not exist");
        }

        UserDTO result = new ModelMapper().map(user, UserDTO.class);
        return result;
    }

    @Override
    public Set<CreateTeamDTO> getTeamsForUser(Principal principal) throws ResourceNotFoundException {
        User user = getUser(principal);

        ModelMapper modelMapper = new ModelMapper();

        Set<CreateTeamDTO> teams = user.getTeamUsers()
                .stream()
                .map(teamUser -> {
                    return modelMapper.map(teamUser.getTeam(), CreateTeamDTO.class);
                })
                .collect(Collectors.toSet());

        return teams;
    }

    @Override
    public UserDTO getUserById(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }
        ModelMapper modelMapper = new ModelMapper();

        UserDTO result = modelMapper.map(user, UserDTO.class);

        return result;
    }

    @Override
    public UserDTO updateUser(Principal principal, UserDTO user) {
        UserDTO result = new UserDTO();

        try {
            User userEntity = getUser(principal);
            userEntity.setFirstName(user.getFirstName());
            userEntity.setLastName(user.getLastName());

            User newUserEntity = userRepository.save(userEntity);

            result = new ModelMapper().map(newUserEntity, UserDTO.class);

        } catch (ResourceNotFoundException ex) {
            logger.log(Level.SEVERE, "User does not exist!");
            throw new ResourceNotFoundException(ex.getMessage());
        }

        return result;
    }

    @Override
    public List<UserDTO> getUsers(int page, int pageSize) {
        List<UserDTO> result = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        if (page > 0) page = page - 1;

        Page<User> usersPage = userRepository.findAll(PageRequest.of(page, pageSize));
        List<User> users = usersPage.getContent();

        result = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public boolean verifyEmailToken(String token) {
        boolean returnValue = false;

        // Find user by token
        User userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            boolean hastokenExpired = jwtUtils.hasTokenExpired(token);
            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

        return returnValue;
    }

}