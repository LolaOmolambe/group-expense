package com.expense.tracker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "standup_update")
@SQLDelete(sql = "UPDATE standup_update SET deleted=true WHERE standup_id=?")
@Where(clause = "deleted = false")
public class StandupUpdate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "standup_id")
    private Long Id;

    @Column(length = 1000, nullable = false)
    private String description;

    @ManyToOne
    private TeamUser teamUser;

    @Builder.Default
    private Boolean deleted = false;
}
