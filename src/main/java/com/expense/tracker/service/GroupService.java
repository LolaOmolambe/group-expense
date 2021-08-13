package com.expense.tracker.service;

import com.expense.tracker.dto.CreateGroup;
import com.expense.tracker.entity.Group;
import com.expense.tracker.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements IGroupService{

    @Autowired
    private GroupRepository groupRepository;


    @Override
    public List<Group> getAllGroups() {
        return this.groupRepository.findByActiveTrue();
    }

    @Override
    public void createGroup(CreateGroup group) {

    }
}
