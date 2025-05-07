package com.three.recipingrecipeservicebe.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class RecipeListResponseDto {

    private Long id;
    private Long userId; // 또는 작성자 닉네임 등으로 변경 가능
    private String title;
    private String imageUrl;
    private String difficulty;
    private Integer cookingTime;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
