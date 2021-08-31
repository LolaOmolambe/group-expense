package com.expense.tracker.controller;

import com.expense.tracker.dto.CreateTeamDTO;
import com.expense.tracker.dto.UserDTO;
import com.expense.tracker.dto.response.ErrorResponse;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/user")
@Tag(name = "user", description = "Users API")
public class UserController {
    @Autowired
    private IUserService userService;


    @PreAuthorize("hasRole('ADMIN') or principal.id == #id")
    @Operation(
            summary = "Details about a User",
            description = "Get Details about a user",
            tags = {"user"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid Credentials",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "User not Found",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @GetMapping(path = "/{id}")
    public UserDTO getUser(@Parameter(description = "User Id") @PathVariable Long id) {
        return userService.getUserById(id);
    }


    @Operation(
            summary = "Update User Details",
            description = "Update Logged in User Details",
            tags = {"user"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "Un-authenticated",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "User not Found",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @PutMapping(path = "/update")
    public UserDTO updateUser(Principal principal, @RequestBody UserDTO userDTO) {
        return userService.updateUser(principal, userDTO);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get All Users",
            description = "Get All Users For Admin",
            tags = {"user"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Operation Successful",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
                            }
                            //schema = @Schema(implementation = Book.class)) })
                            ///content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
                    )}
    )
    public List<UserDTO> getUsers(@Parameter(description = "Page Number")
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @Parameter(description = "Number of Records to return") @RequestParam(value = "size", defaultValue = "10") int size) {
        return userService.getUsers(page, size);
    }


    @GetMapping(path = "/teams")
    @Operation(
            summary = "User's Teams ",
            description = "Get All Teams a Logged in User belongs to",
            tags = {"user"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CreateTeamDTO.class))
                            )
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "Un-authenticated",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "User not Found",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    public Set<CreateTeamDTO> getTeamsBelongingToUser(Principal principal) throws ResourceNotFoundException {
        return userService.getTeamsForUser(principal);
    }

}
