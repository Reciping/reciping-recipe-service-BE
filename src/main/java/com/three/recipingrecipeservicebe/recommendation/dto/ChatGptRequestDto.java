package com.three.recipingrecipeservicebe.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatGptRequestDto {
    private Long totalRecipeCount; // 전체 레시피 수
    private Long pickCount;        // 뽑을 숫자 개수
}
