package com.three.recipingrecipeservicebe.mainPage.dto;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeSummaryResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainPageResponseDto {
    private List<AdResponse> ads;
    private List<EventSummaryResponseDto> events;
    private List<RecipeSummaryResponseDto> recommendedRecipes;
}
