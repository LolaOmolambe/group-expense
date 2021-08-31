package com.expense.tracker.controller;

import com.expense.tracker.dto.*;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(path = "/api/v1/user")
@Tag(name= "user", description = "Users API")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping(path = "/team" )
    public Set<CreateTeamDTO> getTeamsBelongingToUser(Principal principal) throws ResourceNotFoundException {
        return userService.getTeamsForUser(principal);
    }

    //TODO: ONLY FOR ADMIN and logged in user
    @Operation(
            summary = "The Get User Details Web Service Endpoint ",
            security = {
                    @SecurityRequirement(name = "bearerAuth")}
    )
    @GetMapping(path = "/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping(path = "/update")
    public UserDTO  updateUser(Principal principal, @RequestBody UserDTO userDTO){
        return userService.updateUser(principal, userDTO);
    }

    @GetMapping()
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
    //@Operation(summary = "Find Contacts by name", description = "Name search by %name% format", tags = { "contact" })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation",
//                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Contact.class)))) })
    public List<UserDTO> getUsers(@Parameter(description = "Page Number")
            @RequestParam(value = "page", defaultValue = "0")int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size){
        return userService.getUsers(page,size);
    }



}
