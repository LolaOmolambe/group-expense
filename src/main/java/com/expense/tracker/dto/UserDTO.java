package com.expense.tracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.SecondaryTable;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;

    private String firstName;
    private String lastName;
    private String email;

    @JsonIgnore
    private Set<CreateTeamDTO> teams;

}
