package com.three.recipingrecipeservicebe.recipeDetailPage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class RecipeLikeStatusResponseDto {
    private Long recipeId;
    private Long likeCount;
    private boolean isLiked;
}
