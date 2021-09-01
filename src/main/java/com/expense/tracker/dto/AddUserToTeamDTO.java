package com.expense.tracker.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class AddUserToTeamDTO {

    @NotNull(message = "Please enter team id")
    private Long teamId;

    @NotNull(message = "Please enter user id")
    private Long userId;
}
