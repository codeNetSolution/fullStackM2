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

    public Page<CategoryDTO> getAllCategoriesPaginated(int page, int size, String nameFilter, String dateFilter) {
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
        return categoryRepository.findByFilters(nameFilter, startOfDay, startOfNextDay, pageable)
                .map(this::convertToDTO);
    }



    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public CategoryDTO convertToDTO(Category category) {
        List<CategoryDTO> childCategoryDTOs = category.getChildCategories().stream()
                .map(ch -> convertToDTO(ch.getChildCategory()))
                .collect(Collectors.toList());

        Long parentId = category.getParentCategories().stream()
                .findFirst()
                .map(ch -> ch.getParentCategory().getId())
                .orElse(null);

        CategoryDTO categoryDTO = new CategoryDTO(
                category.getId(),
                category.getNom(),
                category.getCreationDate(),
                category.getParentId(),
                childCategoryDTOs,
                category.getParentId() == null
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

        boolean associationExists = categoryHierarchyRepository.findAll().stream()
                .anyMatch(ch -> ch.getParentCategory().equals(parentCategory) && ch.getChildCategory().equals(childCategory));

        if (associationExists) {
            throw new Exception("Cette catégorie enfant a déjà un parent.");
        }

        CategoryHierarchy hierarchy = new CategoryHierarchy();
        hierarchy.setParentCategory(parentCategory);
        hierarchy.setChildCategory(childCategory);
        categoryHierarchyRepository.save(hierarchy);
    }
}
