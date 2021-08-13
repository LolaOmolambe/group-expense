package com.expense.tracker.entity;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long Id;

    @NotEmpty
    @Column(name = "category_name")
    private String categoryName;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
