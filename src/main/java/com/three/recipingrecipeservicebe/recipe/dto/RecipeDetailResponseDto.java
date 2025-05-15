package com.three.recipingrecipeservicebe.recipe.dto;

import com.three.recipingrecipeservicebe.recipe.entity.CookingTime;
import com.three.recipingrecipeservicebe.recipe.entity.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecipeDetailResponseDto {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String cookingTime;
    private String difficulty;
    private String dishType;
    private String situationType;
    private String methodType;
    private String ingredientType;
    private String objectName;
    private String keyName;
    private String filePath;
    private String imageUrl;
    private Long likeCount;
    private List<String> tags;
    private boolean isBookmarked;
    private boolean isLiked;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
