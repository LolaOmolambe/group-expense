package com.expense.tracker.entity;

import com.expense.tracker.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    public Long Id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "role_name")
    private RoleType name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "roles_authorities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    public Role() {
    }

    public Role(RoleType name) {
        this.name = name;
    }

    public Role(Long id, RoleType name) {
        Id = id;
        this.name = name;
    }


}
