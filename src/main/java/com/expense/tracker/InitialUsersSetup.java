package com.expense.tracker;

import com.expense.tracker.entity.Authority;
import com.expense.tracker.entity.Role;
import com.expense.tracker.entity.User;
import com.expense.tracker.enums.RoleType;
import com.expense.tracker.repository.AuthorityRepository;
import com.expense.tracker.repository.RoleRepository;
import com.expense.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class InitialUsersSetup {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        System.out.println("here");
        Authority readAuth = createAuthority("READ_AUTHORITY");
        Authority writeAuth = createAuthority("WRITE_AUTHORITY");
        Authority deleteAuth = createAuthority("DELETE_AUTHORITY");

        Role userRole = createRole(RoleType.User, new HashSet<Authority>(Arrays.asList(readAuth, writeAuth)) );
        Role adminRole = createRole(RoleType.Admin, new HashSet<Authority>(Arrays.asList(readAuth, writeAuth, deleteAuth)) );
        Role superRole = createRole(RoleType.SuperAdmin, new HashSet<Authority>(Arrays.asList(readAuth, writeAuth, deleteAuth)) );

        if(superRole == null) return;

        User adminUser = new User();
        adminUser.setFirstName("Lola");
        adminUser.setLastName("Omolambe");
        adminUser.setEmail("admin@test.com");
        adminUser.setPassword(encoder.encode("12345678"));
        adminUser.setRoles(new HashSet<Role>(Arrays.asList(superRole)));

        Optional<User> storedUserDetails = userRepository.findByEmail("admin@test.com");
        if (!storedUserDetails.isPresent()) {
            userRepository.save(adminUser);
        }
    }

    @Transactional
    private Authority createAuthority(String name) {
        Authority authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new Authority(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    private Role createRole(
            RoleType name, Set<Authority> authorities) {

        Optional<Role> role = roleRepository.findByName(name);

        if(role.isPresent()) return role.get();

        Role newrole = new Role(name);
        if (!role.isPresent()) {

            newrole.setAuthorities(authorities);

            roleRepository.save(newrole);

        }
        return newrole;
    }
}
