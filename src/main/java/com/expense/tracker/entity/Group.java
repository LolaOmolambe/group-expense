package com.expense.tracker.entity;


import com.expense.tracker.enums.GroupType;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long Id;

    @NotEmpty
    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_type")
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    private boolean active;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
