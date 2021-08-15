package com.expense.tracker.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import java.util.Date;

@Getter
@Setter
public class ErrorResponse {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    public ErrorResponse(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }



}
