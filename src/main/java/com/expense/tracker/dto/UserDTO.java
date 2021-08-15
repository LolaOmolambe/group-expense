package com.expense.tracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.SecondaryTable;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;

    private String firstName;
    private String lastName;
    private String email;

    @JsonIgnore
    private Set<CreateTeamDTO> teams;

}
