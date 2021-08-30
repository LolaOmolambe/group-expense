package com.expense.tracker.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SignupRequestModel {
    @NotBlank(message = "Please provide first name")
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank(message = "Please provide last name")
    @Size(min = 3, max = 20)
    private String lastName;

    @NotBlank(message = "Please provide valid email")
    @Email
    private String email;

    private Set<String> role;

    @NotBlank(message = "Please provide password")
    @Size(min = 6, max = 40)
    private String password;








}
