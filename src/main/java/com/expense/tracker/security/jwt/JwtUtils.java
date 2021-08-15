package com.expense.tracker.security.jwt;

import com.expense.tracker.exception.AuthException;
import com.expense.tracker.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public boolean validateJwtToken (String authToken) throws AuthException {
        System.out.println("here " + authToken);
        try {
            System.out.println("here " + jwtSecret);

            Object y = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            System.out.println("herehhh " + y);
            return true;
        } catch (SignatureException e) {
            logger.log(Level.SEVERE,"Invalid JWT signature ", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.log(Level.SEVERE,"Invalid JWT token ", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.log(Level.SEVERE,"JWT token is expired", e.getMessage());
            throw new AuthException("JWT token is expired");
        } catch (UnsupportedJwtException e) {
            logger.log(Level.SEVERE,"JWT token is unsupported", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE,"JWT claims string is empty", e.getMessage());
        }
        return false;
    }

    public String getAuthDetails() {
        Object authenticationObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserDetailsImpl) authenticationObject).getEmail();
    }

}
