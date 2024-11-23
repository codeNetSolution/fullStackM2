package com.CodeNet.FullStackM2.Entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<CategoryHierarchy> childCategories;

    @OneToMany(mappedBy = "childCategory", cascade = CascadeType.ALL)
    private List<CategoryHierarchy> parentCategories;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<CategoryHierarchy> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryHierarchy> childCategories) {
        this.childCategories = childCategories;
    }

    public List<CategoryHierarchy> getParentCategories() {
        return parentCategories;
    }

    public void setParentCategories(List<CategoryHierarchy> parentCategories) {
        this.parentCategories = parentCategories;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getParentId() {
        return this.parentCategory != null ? this.parentCategory.getId() : null;
    }
}
