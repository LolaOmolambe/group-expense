package com.expense.tracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private String status;
    //private String message;
    private ErrorResponse errors;
    private T data;

    public Response(String status, T data) {
        this.status = status;
        //this.message = message;
        this.data = data;
    }

    public Response(String status, ErrorResponse data) {
        this.status = status;
        this.errors = data;
    }


}
