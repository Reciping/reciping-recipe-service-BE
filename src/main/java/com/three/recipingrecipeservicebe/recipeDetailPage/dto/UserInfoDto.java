package com.three.recipingrecipeservicebe.recipeDetailPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {
    private Long userId;
    private SexType sex;
    private AgeType age;
    private InterestKeywordType interestKeyword;

}
