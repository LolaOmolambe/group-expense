package com.expense.tracker.entity;

import com.expense.tracker.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@SQLDelete(sql = "UPDATE roles SET deleted=true WHERE role_id=?")
@Where(clause = "deleted = false")
public class Role extends BaseEntity{
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

    @Builder.Default
    private Boolean deleted = false;

}
