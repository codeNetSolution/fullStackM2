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
            "(:isRoot IS NULL OR c.isRoot = :isRoot) AND " +
            "(:afterDate IS NULL OR c.creationDate >= :afterDate) AND " +
            "(:beforeDate IS NULL OR c.creationDate <= :beforeDate) AND " +
            "(:creationDate IS NULL OR c.creationDate = :creationDate)")
    Page<Category> findByFilters(
            @Param("name") String name,
            @Param("isRoot") Boolean isRoot,
            @Param("afterDate") Date afterDate,
            @Param("beforeDate") Date beforeDate,
            @Param("creationDate") Date creationDate,
            Pageable pageable
    );


}