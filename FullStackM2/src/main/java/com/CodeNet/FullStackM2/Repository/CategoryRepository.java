package com.CodeNet.FullStackM2.Repository;

import com.CodeNet.FullStackM2.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE " +
            "(:name IS NULL OR c.nom LIKE %:name%) AND " +
            "(:startOfDay IS NULL OR (c.creationDate >= :startOfDay AND c.creationDate < :startOfNextDay))" +
             "AND (:isRoot IS NULL OR c.isRoot = :isRoot)")
    Page<Category> findByFilters(@Param("name") String name,
                                 @Param("startOfDay") Date startOfDay,
                                 @Param("startOfNextDay") Date startOfNextDay,
                                 @Param("isRoot") Boolean isRoot,
                                 Pageable pageable);
}