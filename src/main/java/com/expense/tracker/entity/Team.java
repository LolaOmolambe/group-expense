package com.expense.tracker.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teams")
@SQLDelete(sql = "UPDATE teams SET deleted=true WHERE team_id=?")
@Where(clause = "deleted = false")
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long Id;

    @NotEmpty
    @Column(name = "team_name", length = 120, nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_teams",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<TeamUser> teamUsers = new HashSet<>();


    private Boolean active;

    @Builder.Default
    @Column(name = "deleted")
    private Boolean deleted = false;

}
