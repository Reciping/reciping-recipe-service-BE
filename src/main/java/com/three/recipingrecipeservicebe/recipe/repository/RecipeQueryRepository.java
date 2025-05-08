package com.three.recipingrecipeservicebe.recipe.repository;

import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeQueryRepository {

    List<Recipe> findPagedByCreatedAtDesc(Pageable pageable);

}
