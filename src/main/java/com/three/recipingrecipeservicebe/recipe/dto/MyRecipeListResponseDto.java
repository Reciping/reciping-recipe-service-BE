package com.three.recipingrecipeservicebe.recipe.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyRecipeListResponseDto {

    private List<MyRecipeSummaryResponseDto> recipes;
    private int page;
    private int totalPages;

}