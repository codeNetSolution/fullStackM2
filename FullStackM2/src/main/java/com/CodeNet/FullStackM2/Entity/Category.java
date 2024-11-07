package com.CodeNet.FullStackM2.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<Category> childCetegories;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    public boolean isRootCategory() {
        return this.parentCategory == null;
    }
}
