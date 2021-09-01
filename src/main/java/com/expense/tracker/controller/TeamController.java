package com.expense.tracker.controller;

import com.expense.tracker.dto.AddUserToTeamDTO;
import com.expense.tracker.dto.CreateTeamDTO;
import com.expense.tracker.dto.response.ErrorResponse;
import com.expense.tracker.service.interfaces.ITeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/team")
@Tag(name = "team", description = "Team API")
public class TeamController {

    @Autowired
    private ITeamService teamService;

    @Operation(
            summary = "Create Team",
            description = "Team Creation",
            tags = {"team"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateTeamDTO.class))
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "Un-authenticated",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "409", description = "Team already exists",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @PostMapping
    public CreateTeamDTO createTeam(@Validated @RequestBody CreateTeamDTO createTeamDTO, Principal principal) {
        return teamService.createTeam(createTeamDTO, principal);
    }

    @Operation(
            summary = "Details about a Team",
            description = "Get Details about a team and it's members",
            tags = {"team"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateTeamDTO.class))
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid Credentials",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Team not Found",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @GetMapping(path = "/{id}")
    public CreateTeamDTO getTeamById(@Parameter(description = "Team Id") @PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @Operation(
            summary = "Add User to a Team",
            description = "Add User to a team",
            tags = {"team"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid Credentials",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Team/User not Found",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "409", description = "User already part of team",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @PostMapping(path = "/add_user_to_team")
    public void addUserToTeam(@Validated @RequestBody AddUserToTeamDTO addUserToTeamDTO, Principal principal) {
        teamService.addUserToTeam(addUserToTeamDTO.getTeamId(), addUserToTeamDTO.getUserId(), principal);
    }
}
