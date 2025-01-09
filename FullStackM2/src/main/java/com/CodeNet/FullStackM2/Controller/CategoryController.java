package com.CodeNet.FullStackM2.Controller;

import com.CodeNet.FullStackM2.DTO.CategoryDTO;
import com.CodeNet.FullStackM2.Entity.Category;
import com.CodeNet.FullStackM2.Service.CategoryService;
import com.CodeNet.FullStackM2.utils.ApiMessage;
import com.CodeNet.FullStackM2.utils.PaginatedResponse;
import com.CodeNet.FullStackM2.utils.ResponseMessage;
import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/v1/category")
@Tag(name = "Category Management", description = "API for managing categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/filtered")
    public ResponseEntity<PaginatedResponse<CategoryDTO>> getAllCategoriesPaginated(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "name", required = false) String nameFilter,
            @RequestParam(value = "creationDate", required = false) String dateFilter,
            @RequestParam(value = "isRoot", required = false) String isRootParam
    ) {
        Boolean isRoot = null;
        if ("true".equalsIgnoreCase(isRootParam)) {
            isRoot = true;
        } else if ("false".equalsIgnoreCase(isRootParam)) {
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
    @Operation(
            summary = "Créer une nouvelle catégorie",
            description = "Permet de créer une nouvelle catégorie. Les catégories racines (root = true) ne peuvent pas avoir de parent."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Catégorie créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide, par exemple si une catégorie racine a un parent"),
            @ApiResponse(responseCode = "404", description = "La catégorie parente spécifiée n'existe pas"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ApiMessage<Category>> create(
            @Parameter(description = "Détails de la catégorie à créer", required = true)
            @RequestBody Category category) {
        System.out.println("categorie details: " + category.toString());
        try {
            if (category.isRoot()) {
                if (category.getParentID() != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiMessage<>("Une catégorie racine (root = true) ne peut pas avoir de parent.", null, HttpStatus.BAD_REQUEST.value()));
                }
                category.setParentID(null);
                category.setParentCategory(null);
            } else {
                if (category.getParentID() != null) {
                    boolean parentExists = categoryService.categoryExists(category.getParentID());
                    if (!parentExists) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiMessage<>("La catégorie parente spécifiée n'existe pas.", null, HttpStatus.NOT_FOUND.value()));
                    }
                    Category parentCategory = categoryService.getCategoryById(category.getParentID());
                    if (!parentCategory.isRoot()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ApiMessage<>("La catégorie parente spécifiée doit être une racine (root = true).", null, HttpStatus.BAD_REQUEST.value()));
                    }
                    category.setParentCategory(parentCategory);
                }
            }
            Category savedCategory = categoryService.createCategory(category);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiMessage<>("Catégorie créée avec succès.", savedCategory, HttpStatus.CREATED.value()));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiMessage<>("Erreur lors de la création de la catégorie : " + ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour une catégorie",
            description = "Met à jour les détails d'une catégorie existante. L'identifiant de la catégorie doit être fourni dans l'URL."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catégorie mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide, par exemple si les données sont incorrectes"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "Identifiant de la catégorie à mettre à jour", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Détails de la catégorie à mettre à jour", required = true)
            @RequestBody Category category
    ) throws Exception {
        Long parentId = category.getParentID();
        return new ResponseEntity<>(categoryService.updateCategory(id, category.getNom(), parentId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une catégorie",
            description = "Supprime une catégorie existante en fonction de son identifiant fourni dans l'URL."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Catégorie supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Identifiant de la catégorie à supprimer", required = true, example = "1")
            @PathVariable Long id
    ) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(
            summary = "Récupérer toutes les catégories paginées",
            description = "Retourne une liste paginée de toutes les catégories en fonction des paramètres de pagination fournis."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "Paramètres de pagination invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<CategoryDTO>> getAllCategories(
            @Parameter(description = "Numéro de la page (index basé sur 0)", example = "0")
            @RequestParam int page,

            @Parameter(description = "Nombre d'éléments par page", example = "10")
            @RequestParam int size
    ) {
        List<CategoryDTO> categories = categoryService.getAllCategoriesPaginated(page, size).stream()
                .map(categoryService::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer les détails d'une catégorie",
            description = "Retourne les informations détaillées d'une catégorie existante à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Détails de la catégorie récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée pour l'identifiant fourni"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<CategoryDTO> getCategoryDetails(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryDetails(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @PutMapping("/{parentId}/associate/{childId}")
    @Operation(
            summary = "Associer une catégorie parent à une catégorie enfant",
            description = "Permet d'associer une catégorie parent existante avec une catégorie enfant existante. Une catégorie ne peut pas être son propre parent."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Association réussie entre la catégorie parent et enfant"),
            @ApiResponse(responseCode = "400", description = "Requête invalide, par exemple si le parent et l'enfant sont identiques"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponseMessage> associateParentWithChild(
            @Parameter(description = "Identifiant de la catégorie parent", required = true, example = "1")
            @PathVariable Long parentId,

            @Parameter(description = "Identifiant de la catégorie enfant", required = true, example = "2")
            @PathVariable Long childId
    ) {
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
