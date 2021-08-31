package com.expense.tracker.controller;

import com.expense.tracker.dto.JwtResponse;
import com.expense.tracker.dto.LoginRequest;
import com.expense.tracker.dto.UserDTO;
import com.expense.tracker.dto.request.SignupRequestModel;
import com.expense.tracker.dto.response.ErrorResponse;
import com.expense.tracker.exception.AuthException;
import com.expense.tracker.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@Tag(name = "auth", description = "Authentication API")
public class AuthController {
    @Autowired
    private IUserService userService;


    @PostMapping(path = "/signup")
    @Operation(
            summary = "Sign up User",
            description = "Create a user",
            tags = {"auth"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))
                            }
                    ),
                    @ApiResponse(responseCode = "409", description = "Email already exists",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Validation Error",
                            content = @Content)
            }
    )
    public UserDTO registerUser(@Validated @RequestBody SignupRequestModel signupRequestModel) {
        return userService.registerUser(signupRequestModel);
    }

    @PostMapping(path = "/login")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(
            summary = "Login User",
            description = "Login a user",
            tags = {"auth"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponse.class))
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid Credentials",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "400", description = "Validation Error",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    public JwtResponse loginUser(@Validated @RequestBody LoginRequest loginRequest) throws AuthException {
        return userService.loginUser(loginRequest);
    }

//    @GetMapping(path = "/email-verification")
//    @ResponseStatus(code = HttpStatus.OK)
//    public JwtResponse verifyEmail(@RequestParam(value = "token") String token)  {
//        boolean isVerified = userService.verifyEmailToken(token);
//        return userService.loginUser(loginRequest);
//    }


}
