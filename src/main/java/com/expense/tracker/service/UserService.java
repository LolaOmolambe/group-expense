package com.expense.tracker.service;

import com.expense.tracker.dto.*;
import com.expense.tracker.entity.Role;
import com.expense.tracker.entity.Team;
import com.expense.tracker.entity.User;
import com.expense.tracker.enums.RoleType;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.exception.DuplicateEntityException;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.RoleRepository;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.security.jwt.JwtUtils;
import com.expense.tracker.security.services.UserDetailsImpl;
import com.expense.tracker.service.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public UserDTO registerUser(SignupRequest signupRequest) throws DuplicateEntityException, Exception {
        try{

            logger.log(Level.INFO, "Registering User");

            if(userRepository.existsByEmail(signupRequest.getEmail())) {
                logger.log(Level.SEVERE, "Email already exists");
                throw new DuplicateEntityException("Email already exists");
            }

            //Create Account
            User user = new User(signupRequest.getFirstName(), signupRequest.getLastName(),
                    signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

            Set<String> strRoles = signupRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if(strRoles == null) {
                Role userRole = roleRepository.findByName(RoleType.User)
                        .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                roles.add(userRole);

            } else {
                strRoles.forEach(role -> {
                    switch(role) {
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
            userRepository.save(user);

            ModelMapper modelMapper = new ModelMapper();

            UserDTO result = modelMapper.map(user, UserDTO.class);

            logger.log(Level.INFO, "User registered successfully");
            return result;

        }
        catch(DuplicateEntityException ex) {
            logger.log(Level.SEVERE, "Duplicate key ", ex.getMessage());
            throw new DuplicateEntityException(ex.getMessage());
        }
        catch(Exception ex) {
            logger.log(Level.SEVERE, "error occured", ex.getMessage());
            throw new Exception("An error occured.", ex);
        }
    }

    @Override
    public JwtResponse loginUser(LoginRequest loginRequest) throws AuthException {
        try{
            logger.log(Level.INFO, "Logging User in");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);
            Date expiresIn = jwtUtils.getExpirationFromJwtToken(jwt);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

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

        } catch(AuthenticationException ex){
            logger.log(Level.SEVERE, "Invalid credentials", ex.getMessage());
            throw new AuthException("Invalid Credentials");
        }
    }

    @Override
    public User getUser(Principal principal) throws ResourceNotFoundException{
        String loggedInUser = jwtUtils.getAuthDetails();
        User user = this.userRepository.findByEmail(loggedInUser).orElse(null);

        if (user == null) {
            throw new ResourceNotFoundException("Invalid user.");
        } else {
            return user;
        }
    }


}
