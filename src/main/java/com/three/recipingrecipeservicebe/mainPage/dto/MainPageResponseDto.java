package com.three.recipingrecipeservicebe.mainPage.dto;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeSummaryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageResponseDto {
    private List<AdResponse> ads;
    private List<EventSummaryResponseDto> events;
    private List<RecipeSummaryResponseDto> recommendedRecipeList;
}
