package com.expense.tracker.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
public class CreateTeamDTO {

    @NotEmpty(message = "Team name is required")
    private String teamName;

    private Long Id;

    //@NotEmpty(message = "Group type is required")
    //private GroupType groupType;

    private Boolean active = true;

    private Set<UserDTO> users;


}
