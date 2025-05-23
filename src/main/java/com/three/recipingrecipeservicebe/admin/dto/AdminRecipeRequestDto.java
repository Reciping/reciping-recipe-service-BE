package com.three.recipingrecipeservicebe.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminRecipeRequestDto {

    private String title;
    private String content;

}
