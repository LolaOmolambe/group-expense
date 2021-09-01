package com.expense.tracker.exception;

import com.expense.tracker.dto.response.ErrorResponse;
import com.expense.tracker.dto.response.Response;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class ControllerExceptionHandler implements ResponseBodyAdvice<Object> {

    private static final Logger logger = Logger.getLogger(ControllerExceptionHandler.class.getName());

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse message = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        List<ErrorResponse> listofErrors = Arrays.asList(message);

        Response response = Response.builder().success(false).errors(listofErrors).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = {AuthException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> authException(AuthException ex, WebRequest request) {

        ErrorResponse message = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        List<ErrorResponse> listofErrors = Arrays.asList(message);

        Response response = Response.builder().success(false).errors(listofErrors).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException ex, WebRequest request) {

        ErrorResponse message = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        List<ErrorResponse> listofErrors = Arrays.asList(message);

        Response response = Response.builder().success(false).errors(listofErrors).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {DuplicateEntityException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<Object> duplicateException(DuplicateEntityException ex, WebRequest request) {

        ErrorResponse message = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        List<ErrorResponse> listofErrors = Arrays.asList(message);

        Response response = Response.builder().success(false).errors(listofErrors).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    //String fieldname = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return errorMessage;
                })
                .collect(Collectors.toList());

        ErrorResponse message = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                //"Validation Error",
                errors.toString(),
                request.getDescription(true)
        );
        List<ErrorResponse> listofErrors = Arrays.asList(message);

        Response response = Response.builder().success(false).errors(listofErrors).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> globalExceptionHandler(Exception ex, WebRequest request) {
        System.out.println(ex.getClass().getCanonicalName());
        ErrorResponse message = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        List<ErrorResponse> listofErrors = Arrays.asList(message);

        Response response = Response.builder().success(false).errors(listofErrors).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof Response) {
            return o;
        } else if (o instanceof String) {
            return o;
        } else if (o instanceof TreeMap) {
            return o;
        } else {
            return Response.builder().success(true).result(o).build();
        }
    }
}
