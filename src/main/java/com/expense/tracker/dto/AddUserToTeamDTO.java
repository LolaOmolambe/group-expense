package com.expense.tracker.dto;


import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddUserToTeamDTO {

    @NotNull(message = "Please enter team id")
    private Long teamId;

    @NotNull(message = "Please enter user id")
    private Long userId;
}
