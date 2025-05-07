package com.three.recipingrecipeservicebe.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class RecipeDetailResponseDto {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Integer cookingTime;
    private String difficulty;
    private String category;
    private String occasion;
    private String method;
    private String ingredient;
    private String objectName;
    private String keyName;
    private String filePath;
    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
