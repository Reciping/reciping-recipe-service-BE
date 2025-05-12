package com.three.recipingrecipeservicebe.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RecipeCountResponseDto {

    private Long userId;
    private Long totalRecipeCount;

}