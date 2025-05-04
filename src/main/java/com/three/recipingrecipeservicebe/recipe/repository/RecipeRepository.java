package com.three.recipingrecipeservicebe.recipe.repository;

import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
