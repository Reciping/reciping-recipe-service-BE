package com.three.recipingrecipeservicebe.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecipeResponseDto {

    private Long id;
    private Long userId;
    private String title;
    private String imageUrl;
    private String difficulty;
    private Integer cookingTime;
    private int likeCount;
    private boolean isLiked;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
