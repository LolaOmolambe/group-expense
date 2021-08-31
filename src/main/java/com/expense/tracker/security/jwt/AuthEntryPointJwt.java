package com.expense.tracker.security.jwt;

import com.expense.tracker.dto.response.ErrorResponse;
import com.expense.tracker.dto.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = Logger.getLogger(AuthEntryPointJwt.class.getName());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        logger.log(Level.SEVERE, "User not authenticated", e.getMessage());

        ErrorResponse errorMessage = ErrorResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(new Date()).message(e.getMessage())
                .build();

        List<ErrorResponse> listofErrors = Arrays.asList(errorMessage);

        Response customResponse = Response.builder().success(false).errors(listofErrors).build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, customResponse);
        out.flush();

    }
}
