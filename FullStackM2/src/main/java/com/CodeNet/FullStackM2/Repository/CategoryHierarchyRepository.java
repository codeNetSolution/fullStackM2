package com.CodeNet.FullStackM2.Repository;

import com.CodeNet.FullStackM2.Entity.CategoryHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryHierarchyRepository extends JpaRepository<CategoryHierarchy, Long> {
}

