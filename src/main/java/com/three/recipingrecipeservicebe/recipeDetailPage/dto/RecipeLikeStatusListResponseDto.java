package com.three.recipingrecipeservicebe.recipeDetailPage.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class RecipeLikeStatusListResponseDto {
    private List<RecipeLikeStatusResponseDto> data;
}
