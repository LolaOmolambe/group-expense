package com.expense.tracker.controller;

import com.expense.tracker.dto.CreateGroup;
import com.expense.tracker.entity.Group;
import com.expense.tracker.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/group")
public class GroupController {

    private static final Logger logger = Logger.getLogger(IGroupService.class.getName());


    @Autowired
    private IGroupService groupService;



    @GetMapping
    public ResponseEntity<Object> getActivegroups() {
        logger.log(Level.INFO,"Running all blah blah");
        List<Group> groups =  groupService.getAllGroups();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

//    @RequestMapping(path = "/countries/{countryId}/states", method = RequestMethod.GET)
//    public List<State> getAllStatesInCountry(@PathVariable("countryId") Long countryId) throws ApiException {
//        try {
//            return locationService.getStatesInCountry(countryId);
//        } catch (CosmosServiceException ex) {
//            logger.log(Level.SEVERE, "An unexpected error occurred getting states", ex);
//            throw new ApiException(getStatesFailure, ex);
//        }
//    }

    @PostMapping
    public void create(@RequestBody @Validated final CreateGroup speaker){
         groupService.createGroup(speaker);
    }

}
