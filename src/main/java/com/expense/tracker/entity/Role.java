package com.expense.tracker.entity;

import com.expense.tracker.enums.RoleType;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    public Long Id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "role_name")
    private RoleType name;

    public Role() {
    }

    public Role(Long id, RoleType name) {
        Id = id;
        this.name = name;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }
}
