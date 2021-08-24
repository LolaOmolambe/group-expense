package com.expense.tracker.entity;

import com.expense.tracker.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authorities")
@SQLDelete(sql = "UPDATE authorities SET deleted=true WHERE authority_id=?")
@Where(clause = "deleted = false")
public class Authority extends BaseEntity {
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

    @Builder.Default
    private Boolean deleted = false;
}
