package com.CodeNet.FullStackM2.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.List;

public class CategoryDTO {
    private Long id;
    private String nom;
    private Long parentId;
    private Date creationDate;
    private boolean isRoot;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryDTO> childCategories;




    // Constructeur
    public CategoryDTO(Long id, String nom, Date creationDate, Long parentId, List<CategoryDTO> childCategories, boolean isRoot) {
        this.id = id;
        this.nom = nom;
        this.parentId = parentId;
        this.creationDate = creationDate;
        this.childCategories = childCategories;
        this.isRoot = isRoot;
    }



    // Getters et Setters
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<CategoryDTO> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryDTO> childCategories) {
        this.childCategories = childCategories;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }
    public boolean isRoot() {
        return isRoot;
    }

}
