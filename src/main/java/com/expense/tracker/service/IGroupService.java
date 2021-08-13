package com.expense.tracker.service;

import com.expense.tracker.dto.CreateGroup;
import com.expense.tracker.entity.Group;

import java.util.List;

public interface IGroupService {
    List<Group> getAllGroups();
    void createGroup(CreateGroup group);
}
