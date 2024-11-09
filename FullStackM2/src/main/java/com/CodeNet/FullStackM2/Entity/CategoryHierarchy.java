package com.CodeNet.FullStackM2.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "category_hierarchy")
public class CategoryHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parentCategory;

    @ManyToOne
    @JoinColumn(name = "child_id", referencedColumnName = "id")
    private Category childCategory;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Category getChildCategory() {
        return childCategory;
    }

    public void setChildCategory(Category childCategory) {
        this.childCategory = childCategory;
    }
}
