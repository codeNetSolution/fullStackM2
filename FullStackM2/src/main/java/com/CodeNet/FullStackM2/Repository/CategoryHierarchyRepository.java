package com.CodeNet.FullStackM2.Repository;

import com.CodeNet.FullStackM2.Entity.Category;
import com.CodeNet.FullStackM2.Entity.CategoryHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryHierarchyRepository extends JpaRepository<CategoryHierarchy, Long> {

    @Query("SELECT ch.childCategory FROM CategoryHierarchy ch WHERE ch.parentCategory.id = :parentId")
    List<Category> findChildrenByParentId(@Param("parentId") Long parentId);

    @Query("SELECT ch.parentCategory FROM CategoryHierarchy ch WHERE ch.childCategory.id = :chiledId")
    List<Category> findParentByChildrenId(@Param("chiledId") Long chiledId);

    boolean existsByChildCategory(Category childCategory);
}

