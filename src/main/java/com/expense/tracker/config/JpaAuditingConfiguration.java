package com.expense.tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
//@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaAuditing
public class JpaAuditingConfiguration implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
        //return Optional.empty();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String actor = "SYSTEM";
        if (authentication != null && authentication.getName() != null) {
            actor = authentication.getName();
        }
        return Optional.of(actor);
    }
}
