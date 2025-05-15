package com.three.recipingrecipeservicebe.recipeDetailPage.dto;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecipeDetailAggregateDto {
    private RecipeDetailResponseDto recipe;
    private Page<CommentResponseDto> comments;
}