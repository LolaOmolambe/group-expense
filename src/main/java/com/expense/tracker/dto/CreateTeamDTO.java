package com.expense.tracker.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class CreateTeamDTO {

    @NotEmpty(message = "Team name is required")
    private String teamName;

    private Long Id;

    private Boolean active = true;

    private Set<UserDTO> users;


}
