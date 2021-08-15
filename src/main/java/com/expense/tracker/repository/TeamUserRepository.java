package com.expense.tracker.repository;

import com.expense.tracker.entity.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {

    @Query(value = "SELECT * FROM team_user WHERE team_id = ?1 AND user_id = ?2", nativeQuery = true)
    TeamUser findByTeamAndUser(Long team, Long user);
}
