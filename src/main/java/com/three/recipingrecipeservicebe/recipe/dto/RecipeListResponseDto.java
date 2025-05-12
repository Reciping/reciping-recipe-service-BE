package com.three.recipingrecipeservicebe.recipe.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecipeListResponseDto {

    private List<RecipeSummaryResponseDto> recipes;
    private int page;
    private int totalPages;

}