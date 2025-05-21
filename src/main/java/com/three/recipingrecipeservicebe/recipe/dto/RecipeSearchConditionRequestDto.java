package com.three.recipingrecipeservicebe.recipe.dto;

import com.three.recipingrecipeservicebe.recipe.entity.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecipeSearchConditionRequestDto {
    private DishType dishType;
    private SituationType situationType;
    private IngredientType ingredientType;
    private MethodType methodType;
    private CookingTime cookingTime;
    private Difficulty difficulty;
}
