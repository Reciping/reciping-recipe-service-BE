package com.three.recipingrecipeservicebe.recommendation.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendRequestDto {
    private Long userId;
    private String sexEnum;
    private String ageEnum;
}
