package com.expense.tracker.dto;

import com.expense.tracker.enums.GroupType;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

public class CreateGroup {

    @NotEmpty(message = "Group name is required")
    private String groupName;

    @NotEmpty(message = "Group type is required")
    private GroupType groupType;

    private boolean active;


}
