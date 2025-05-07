package com.three.recipingrecipeservicebe.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class RecipeCreatedResponseDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private String imageUrl;
}
