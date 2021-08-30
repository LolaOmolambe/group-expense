package com.expense.tracker.dto.response;

import lombok.*;
import org.springframework.context.annotation.Scope;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse  {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

}
