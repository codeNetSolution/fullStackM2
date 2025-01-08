package com.CodeNet.FullStackM2.Controller;

import com.CodeNet.FullStackM2.DTO.CategoryDTO;
import com.CodeNet.FullStackM2.Entity.Category;
import com.CodeNet.FullStackM2.Service.CategoryService;
import com.CodeNet.FullStackM2.utils.ApiResponse;
import com.CodeNet.FullStackM2.utils.PaginatedResponse;
import com.CodeNet.FullStackM2.utils.ResponseMessage;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<Category>> create(@RequestBody Category category) {
        try {
            if (category.isRoot()) {
                if (category.getParentID() != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>("Une catégorie racine (root = true) ne peut pas avoir de parent.", null, HttpStatus.BAD_REQUEST.value()));
                }
                category.setParentID(null);
                category.setParentCategory(null);
            } else {
                if (category.getParentID() != null) {
                    boolean parentExists = categoryService.categoryExists(category.getParentID());
                    if (!parentExists) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>("La catégorie parente spécifiée n'existe pas.", null, HttpStatus.NOT_FOUND.value()));
                    }
                    Category parentCategory = categoryService.getCategoryById(category.getParentID());
                    if (!parentCategory.isRoot()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ApiResponse<>("La catégorie parente spécifiée doit être une racine (root = true).", null, HttpStatus.BAD_REQUEST.value()));
                    }
                    category.setParentCategory(parentCategory);
                }
            }
            Category savedCategory = categoryService.createCategory(category);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Catégorie créée avec succès.", savedCategory, HttpStatus.CREATED.value()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Erreur lors de la création de la catégorie : " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) throws Exception {
        Long parentId = category.getParentID();
        return new ResponseEntity<>(categoryService.updateCategory(id, category.getNom(), parentId), HttpStatus.OK);
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
            if (parentId.equals(childId)) {
                return ResponseEntity.badRequest().body(new ResponseMessage("Une catégorie ne peut pas être son propre parent."));
            }
            categoryService.associateParentWithChild(parentId, childId);
            return ResponseEntity.ok(new ResponseMessage("Association réussie entre la catégorie parent et enfant"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Erreur lors de l'association: " + e.getMessage()));
        }
    }

}
