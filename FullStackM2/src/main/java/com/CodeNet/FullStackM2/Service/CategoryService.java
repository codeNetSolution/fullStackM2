package com.CodeNet.FullStackM2.Service;

import com.CodeNet.FullStackM2.DTO.CategoryDTO;
import com.CodeNet.FullStackM2.Entity.Category;
import com.CodeNet.FullStackM2.Entity.CategoryHierarchy;
import com.CodeNet.FullStackM2.Repository.CategoryRepository;
import com.CodeNet.FullStackM2.Repository.CategoryHierarchyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryHierarchyRepository categoryHierarchyRepository;

    public Page<CategoryDTO> getAllCategoriesPaginated(int page, int size, String nameFilter,
                                                       String afterDateFilter, String beforeDateFilter, String creationDateFilter, Boolean isRoot) {
        Pageable pageable = PageRequest.of(page, size);

        Date afterDate = null;
        Date beforeDate = null;
        Date creationDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (afterDateFilter != null && !afterDateFilter.isEmpty()) {
                afterDate = dateFormat.parse(afterDateFilter);
            }
            if (beforeDateFilter != null && !beforeDateFilter.isEmpty()) {
                beforeDate = dateFormat.parse(beforeDateFilter);
            }
            if(creationDateFilter != null && !creationDateFilter.isEmpty()) {
                creationDate = dateFormat.parse(creationDateFilter);
            }
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format, expected YYYY-MM-DD.");
        }

        return categoryRepository.findByFilters(nameFilter, isRoot, afterDate, beforeDate, creationDate, pageable)
                .map(this::convertToDTO);
    }




    public Category createCategory(Category category) {
        if (category.isRoot()) {
            // Une racine ne peut pas avoir de parent
            if (category.getParentID() != null) {
                throw new RuntimeException("Une catégorie racine (root = true) ne peut pas avoir de parent.");
            }
            category.setParentID(null);
            category.setParentCategory(null);
        } else {
            // Une catégorie non racine peut avoir un parent ou être indépendante
            if (category.getParentID() != null) {
                Category parentCategory = categoryRepository.findById(category.getParentID())
                        .orElseThrow(() -> new RuntimeException("Parent category not found"));
                if (!parentCategory.isRoot()) {
                    throw new RuntimeException("La catégorie parente spécifiée doit être une racine (root = true).");
                }
                category.setParentCategory(parentCategory);
            }
        }

        return categoryRepository.save(category);
    }





    public CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getNom(),
                category.getCreationDate(),
                category.getParentId(),
                null,
                category.isRoot(),
                null
        );
    }





    public Category updateCategory(Long id, String nom, Long parentId) throws Exception {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setNom(nom);
            if (parentId != null) {
                Category parentCategory = categoryRepository.findById(parentId)
                        .orElseThrow(() -> new RuntimeException("Parent category not found"));
                associateParentWithChild(parentId, id);
            }
            return categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> getAllCategoriesPaginated(int page, int size) {
        return categoryRepository.findAll();
    }

    public CategoryDTO getCategoryDetails(Long id) {
        // Récupérer la catégorie
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Récupérer les enfants
        List<Category> children = categoryHierarchyRepository.findChildrenByParentId(id);

        List<Category> parent = categoryHierarchyRepository.findParentByChildrenId(id);

        // Convertir les enfants en DTO
        List<CategoryDTO> childCategoryDTOs = children.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        List<CategoryDTO> parentCategoryDTOs = parent.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        // Créer le DTO avec les enfants inclus
        return new CategoryDTO(
                category.getId(),
                category.getNom(),
                category.getCreationDate(),
                category.getParentId(),
                childCategoryDTOs,
                category.isRoot(),
                parentCategoryDTOs
        );
    }

    public void associateParentWithChild(Long parentId, Long childId) {
        if (parentId.equals(childId)) {
            throw new RuntimeException("Une catégorie ne peut pas être son propre parent.");
        }
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent category not found"));

        Category child = categoryRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child category not found"));

        boolean hasExistingParent = categoryHierarchyRepository.existsByChildCategory(child);
        if (hasExistingParent) {
            throw new RuntimeException("Cette catégorie enfant est déjà associée à un parent.");
        }

        if (child.isRoot()) {
            throw new RuntimeException("A root category cannot be a child.");
        }

        CategoryHierarchy hierarchy = new CategoryHierarchy();
        hierarchy.setParentCategory(parent);
        hierarchy.setChildCategory(child);

        categoryHierarchyRepository.save(hierarchy);
    }

    public boolean categoryExists(Long parentId) {
        return categoryRepository.existsById(parentId);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La catégorie spécifiée n'existe pas."));
    }


}
