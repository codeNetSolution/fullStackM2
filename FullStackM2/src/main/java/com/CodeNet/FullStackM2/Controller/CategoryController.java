package com.CodeNet.FullStackM2.Controller;

import com.CodeNet.FullStackM2.Entity.Category;
import com.CodeNet.FullStackM2.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return new ResponseEntity<>(categoryService.updateCategory(id, category.getNom(), category.getParentCategory() != null ? category.getParentCategory().getId() : null), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(categoryService.getAllCategoriesPaginated(page, size), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String filter) {
        return new ResponseEntity<>(categoryService.searchCategoriesWithFilter(filter), HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<Category>> getAllCategoriesSorted(@RequestParam String field, @RequestParam String direction) {
        return new ResponseEntity<>(categoryService.getAllCategoriesSorted(field, direction), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryDetails(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.getCategoryDetails(id), HttpStatus.OK);
    }


}
