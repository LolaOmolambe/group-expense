package com.expense.tracker.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = Logger.getLogger(AuthEntryPointJwt.class.getName());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        logger.log(Level.SEVERE, "Unauthorized error ", e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error : Unauthorized");
    }
}
