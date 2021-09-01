package com.expense.tracker.controller;

import com.expense.tracker.dto.StandupUpdateDTO;
import com.expense.tracker.dto.response.ErrorResponse;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.interfaces.IStandupUpdateService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/standup_update")
@Tag(name = "standup_update", description = "Standup Update API")
public class StandupUpdateController {

    @Autowired
    private IStandupUpdateService standupUpdateService;


    @Operation(
            summary = "Add Update",
            description = "Add Update to a Team",
            tags = {"standup_update"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StandupUpdateDTO.class))
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "Un-authenticated",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Team does not exist. or You are not part of this team.",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @PostMapping
    public StandupUpdateDTO addUpdate(@Validated @RequestBody StandupUpdateDTO standupUpdateDTO, Principal principal) {
        return standupUpdateService.addUpdate(standupUpdateDTO, principal);
    }

    @GetMapping(path = "/{id}")
    //TODO : Only if you are part of the team, see all
    public StandupUpdateDTO getStandupById(@PathVariable Long id) throws ResourceNotFoundException {
        StandupUpdateDTO result = standupUpdateService.getSingleUpdate(id);
        return result;
    }

    @Operation(
            summary = "Get Standups For Team",
            description = "Get Standups For Team",
            tags = {"standup_update"},
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class))
                            }
                    ),
                    @ApiResponse(responseCode = "401", description = "Un-authenticated",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Team does not exist. or You are not part of this team.",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))
                            })
            }
    )
    @GetMapping(path = "/team/{teamId}")
    //TODO : Only if you are part of the team, see all
    public Map<String, Object> getStandupForTeam(@PathVariable Long teamId,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        return standupUpdateService.getStandupForTeam(teamId, page, size);
    }

}
