package com.three.recipingrecipeservicebe.recipe.repository;

import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeQueryRepository {

    Long countByUserIdAndIsDeletedFalse(Long userId);

    Page<Recipe> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);

    Page<Recipe> findByIdInAndIsDeletedFalse(List<Long> ids, Pageable pageable);
}
