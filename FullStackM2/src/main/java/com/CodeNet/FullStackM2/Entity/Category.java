package com.CodeNet.FullStackM2.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "category")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentID;

    @Column(name= "is_root", nullable = false)
    private boolean isRoot;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<CategoryHierarchy> childCategories;

    @OneToMany(mappedBy = "childCategory", cascade = CascadeType.ALL)
    private List<CategoryHierarchy> parentCategories;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @PrePersist
    protected  void onCreate() {
        if (creationDate == null) {
            creationDate = new Date();
        }
    }

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

    public boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public Long getParentID() {
        return this.parentID;
    }

    public Category getParentCategory() {
        return this.parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
}
