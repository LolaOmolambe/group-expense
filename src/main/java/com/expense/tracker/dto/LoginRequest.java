package com.expense.tracker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Please provide the email address")
    @Email(message = "Please provide the email address")
    private String email;

    @NotBlank(message = "Please provide the password")
    private String password;


}
