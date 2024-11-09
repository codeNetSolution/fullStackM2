package com.CodeNet.FullStackM2.DTO;

import java.util.List;

public class CategoryDTO {
    private Long id;
    private String nom;
    private Long parentId;
    private List<CategoryDTO> childCategories;

    // Constructeur
    public CategoryDTO(Long id, String nom, Long parentId) {
        this.id = id;
        this.nom = nom;
        this.parentId = parentId;
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
}
