package com.three.recipingrecipeservicebe.recipeDetailPage.dto;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecipeDetailAggregateDto {
    private RecipeDetailResponseDto recipe;
    private List<CommentResponseDto> comments;
}