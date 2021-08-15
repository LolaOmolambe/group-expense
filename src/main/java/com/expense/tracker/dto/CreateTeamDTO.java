package com.expense.tracker.dto;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class CreateTeamDTO {

    @NotEmpty(message = "Team name is required")
    private String teamName;

    private Long Id;

    //@NotEmpty(message = "Group type is required")
    //private GroupType groupType;

    private Boolean active = true;

    private Set<UserDTO> users;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }
}
