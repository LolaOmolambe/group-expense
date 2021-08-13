package com.expense.tracker.service;

import com.expense.tracker.dto.JwtResponse;
import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.SignupRequest;
import com.expense.tracker.entity.Role;
import com.expense.tracker.entity.User;
import com.expense.tracker.enums.RoleType;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.repository.RoleRepository;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.security.jwt.JwtUtils;
import com.expense.tracker.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService{

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

    @Override
    public User registerUser(SignupRequest signupRequest) {
        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");
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

        return user;
        //return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public JwtResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail(),
                roles);

//        return ResponseEntity.ok(new JwtResponse(jwt,
//                userDetails.getId(),
//                userDetails.getUsername(),
//                userDetails.getEmail(),
//                roles));
    }
}
