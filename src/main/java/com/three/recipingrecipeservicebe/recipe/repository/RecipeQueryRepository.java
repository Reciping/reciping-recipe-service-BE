package com.three.recipingrecipeservicebe.recipe.repository;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeSearchConditionRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeQueryRepository {

    Page<Recipe> findPagedByCreatedAtDesc(Pageable pageable);

    Page<Recipe> searchByCondition(RecipeSearchConditionRequestDto condition, Pageable pageable);

}
