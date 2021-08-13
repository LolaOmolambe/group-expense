package com.expense.tracker.security.jwt;

import com.expense.tracker.security.services.UserDetailsImpl;
import com.expense.tracker.service.IGroupService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtUtils {

    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value(("${app.jwtExpirationMs}"))
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateJwtToken (String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.log(Level.SEVERE,"Invalid JWT signature ", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.log(Level.SEVERE,"Invalid JWT token ", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.log(Level.SEVERE,"JWT token is expired", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.log(Level.SEVERE,"JWT token is unsupported", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE,"JWT claims string is empty", e.getMessage());
        }
        return false;
    }
}
