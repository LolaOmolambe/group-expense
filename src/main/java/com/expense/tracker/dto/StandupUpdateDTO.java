package com.expense.tracker.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class StandupUpdateDTO {

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "Please enter team id")
    private Long teamId;

}
