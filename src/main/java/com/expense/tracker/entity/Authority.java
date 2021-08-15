package com.expense.tracker.entity;

import com.expense.tracker.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    public Long Id;

    @Column(length = 20, name = "authority_name", nullable = false)
    @NotEmpty
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;


    public Authority() {
    }

    public Authority(String name) {
        this.name = name;
    }

}
