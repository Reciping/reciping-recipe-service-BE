package com.three.recipingrecipeservicebe.recipeDetailPage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class LikeStatusResponseDto {
    private long recipeId;
    private long likeCount;
    private boolean isLiked;
}
