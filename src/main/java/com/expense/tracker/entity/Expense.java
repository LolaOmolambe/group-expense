package com.expense.tracker.entity;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long Id;

    @NotEmpty
    private BigDecimal cost;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    private String description;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    private LocalDateTime dateCreated = LocalDateTime.now();

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }
}
