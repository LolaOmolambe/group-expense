package com.expense.tracker.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "team_user")
@SQLDelete(sql = "UPDATE team_user SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class TeamUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", nullable=false, updatable=false, unique = true)
    private Long Id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    private Boolean isAdmin = false;

    @Builder.Default
    private Boolean deleted = false;

    @OneToMany(mappedBy = "teamUser")
    private List<StandupUpdate> standupUpdates;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamUser)) return false;
        TeamUser that = (TeamUser) o;
        return Objects.equals(user.getEmail(), that.user.getEmail()) &&
                Objects.equals(team.getName(), that.team.getName()) &&
                Objects.equals(isAdmin, that.isAdmin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getEmail(), team.getName(), isAdmin);
    }
}
