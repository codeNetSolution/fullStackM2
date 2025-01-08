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

    public Page<CategoryDTO> getAllCategoriesPaginated(int page, int size, String nameFilter, String dateFilter, Boolean isRoot) {
        Pageable pageable = PageRequest.of(page, size);
        Date startOfDay = null;
        Date startOfNextDay = null;

        try {
            if (dateFilter != null && !dateFilter.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                startOfDay = dateFormat.parse(dateFilter);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startOfDay);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                startOfNextDay = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return categoryRepository.findByFilters(nameFilter, startOfDay, startOfNextDay, isRoot, pageable)
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
        List<CategoryDTO> childCategoryDTOs = category.getChildCategories().stream()
                .map(ch -> convertToDTO(ch.getChildCategory()))
                .collect(Collectors.toList());
        Long parentId = category.getParentId();
        CategoryDTO categoryDTO = new CategoryDTO(
                category.getId(),
                category.getNom(),
                category.getCreationDate(),
                parentId,
                childCategoryDTOs,
                category.isRoot()
        );
        categoryDTO.setChildCategories(childCategoryDTOs);

        return categoryDTO;
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

    public Category getCategoryDetails(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void associateParentWithChild(Long parentId, Long childId) throws Exception {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent category not found"));
        Category childCategory = categoryRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("Child category not found"));
        if (childCategory.isRoot()) {
            throw new Exception("Une catégorie racine ne peut pas être assignée comme enfant.");
        }
        if (parentId.equals(childId)) {
            throw new Exception("Une catégorie ne peut pas être son propre parent.");
        }

        childCategory.setParentCategory(parentCategory);
        childCategory.setParentID(parentId);
        categoryRepository.save(childCategory);
    }

    public boolean categoryExists(Long parentId) {
        return categoryRepository.existsById(parentId);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La catégorie spécifiée n'existe pas."));
    }


}
