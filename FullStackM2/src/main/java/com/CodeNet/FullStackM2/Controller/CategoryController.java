package com.CodeNet.FullStackM2.Controller;

import com.CodeNet.FullStackM2.DTO.CategoryDTO;
import com.CodeNet.FullStackM2.Entity.Category;
import com.CodeNet.FullStackM2.Service.CategoryService;
import com.CodeNet.FullStackM2.utils.PaginatedResponse;
import com.CodeNet.FullStackM2.utils.ResponseMessage;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/filtered")
    public ResponseEntity<PaginatedResponse<CategoryDTO>> getAllCategoriesPaginated(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "name", required = false) String nameFilter,
            @RequestParam(value = "creationDate", required = false)  String dateFilter,
            @RequestParam(value = "isRoot", required = false)@Nullable String isRootParam) {


        Boolean isRoot = null;

        if ("true".equalsIgnoreCase(String.valueOf(isRootParam))) {
            isRoot = true;
        } else if ("false".equalsIgnoreCase(String.valueOf(isRootParam))) {
            isRoot = false;
        }

        Page<CategoryDTO> categoryPage = categoryService.getAllCategoriesPaginated(page, size, nameFilter, dateFilter, isRoot);

        PaginatedResponse<CategoryDTO> response = new PaginatedResponse<>(
                categoryPage.getContent(),
                categoryPage.getNumber(),
                categoryPage.getSize(),
                (int) categoryPage.getTotalElements(),
                categoryPage.getTotalPages()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        if (category.isRoot()) {
            category.setParentID(null);
        }
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) throws Exception {
        return new ResponseEntity<>(categoryService.updateCategory(id, category.getNom(), null), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(@RequestParam int page, @RequestParam int size) {
        List<CategoryDTO> categories = categoryService.getAllCategoriesPaginated(page, size).stream()
                .map(categoryService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryDetails(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.convertToDTO(categoryService.getCategoryDetails(id));
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PutMapping("/{parentId}/associate/{childId}")
    public ResponseEntity<ResponseMessage> associateParentWithChild(@PathVariable Long parentId, @PathVariable Long childId) {
        try {
            categoryService.associateParentWithChild(parentId, childId);
            return ResponseEntity.ok(new ResponseMessage("Association réussie entre la catégorie parent et enfant"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Erreur lors de l'association: " + e.getMessage()));
        }
    }
}
