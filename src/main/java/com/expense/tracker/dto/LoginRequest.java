package com.expense.tracker.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Please provide the email address")
    @Email
    private String email;

    @NotBlank(message = "Please provide the password")
    private String password;


}
