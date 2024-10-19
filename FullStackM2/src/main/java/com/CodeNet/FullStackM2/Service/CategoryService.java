package com.CodeNet.FullStackM2.Service;


import com.CodeNet.FullStackM2.Entity.Category;
import com.CodeNet.FullStackM2.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository CategoryRepository;

    public Category createCategory(Category category) {
        return CategoryRepository.save(category);
    }

    public Category updateCategory(Long id, String nom, Long parentId) {
        Optional<Category> categoryOptional = CategoryRepository.findById(id);
        if(categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setNom(nom);
            if(parentId != null) {
                Category parentCategory = CategoryRepository.findById(parentId).orElseThrow();
                category.setParentCategory(parentCategory);
            }
            return CategoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    public void deleteCategory(Long id) {
        CategoryRepository.deleteById(id);
    }

    public List<Category> getAllCategoriesPaginated(int page, int size) {
        return CategoryRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public List<Category> searchCategoriesWithFilter(String filter) {
        // j'aimplimente la logique de filtre
        return CategoryRepository.findAll();
    }

    public List<Category> getAllCategoriesSorted(String field, String direction) {
        // on implimente la logique de tri
        return CategoryRepository.findAll();
    }

    public Category getCategoryDetails(Long id) {
        return CategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
