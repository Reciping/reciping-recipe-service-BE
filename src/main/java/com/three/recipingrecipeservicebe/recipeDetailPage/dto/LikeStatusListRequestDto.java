package com.three.recipingrecipeservicebe.recipeDetailPage.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class LikeStatusListRequestDto {

    private List<Long> recipeIdList;
    private Long userId;
    private int page;
    private int size;

}