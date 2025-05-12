package com.three.recipingrecipeservicebe.recipe.dto;

import com.three.recipingrecipeservicebe.recipe.entity.DishType;
import com.three.recipingrecipeservicebe.recipe.entity.IngredientType;
import com.three.recipingrecipeservicebe.recipe.entity.MethodType;
import com.three.recipingrecipeservicebe.recipe.entity.SituationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecipeSearchConditionRequestDto {
    private DishType dish;
    private SituationType situation;
    private IngredientType ingredient;
    private MethodType method;
}
