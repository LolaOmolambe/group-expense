package com.expense.tracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private boolean success;
    private Object result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder.Default
    private List<ErrorResponse> errors = new ArrayList();

}
