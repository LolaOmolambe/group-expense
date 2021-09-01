package com.expense.tracker.repository;

import com.expense.tracker.entity.StandupUpdate;
import com.expense.tracker.entity.TeamUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandupUpdateRepository extends JpaRepository<StandupUpdate, Long> {
    @Query(value = "select * from standup_update u " +
            "join team_user t on u.team_user_id = t.id " +
            "where t.team_id = ?1",
            countQuery = "select count(*) from standup_update u " +
                    "join team_user t on u.team_user_id = t.id " +
                    "where t.team_id = ?1",
            nativeQuery = true)
    Page<StandupUpdate> findByTeamID(Long teamId, Pageable pageable);

    List<TeamUser> findAllByTeamUser(TeamUser teamUser);
}
