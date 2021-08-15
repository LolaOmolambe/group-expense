package com.expense.tracker.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "team_user")
public class TeamUser {

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

    public TeamUser(User user, Team team, Boolean isAdmin) {
        this.user = user;
        this.team = team;
        this.isAdmin = isAdmin;
    }

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
